package com.example.newjobinsenior

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.newjobinsenior.databinding.ActivityAuthBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeVisibility(intent.getStringExtra("data").toString())

        binding.goSignInBtn.setOnClickListener{
            // 회원가입
            changeVisibility("signin")
        }

        binding.signBtn.setOnClickListener {
            // 이메일,비밀번호 회원가입
            val email:String = binding.authEmailEditView.text.toString()
            val password:String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        MyApplication.auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{
                                sendTask ->
                            if(sendTask.isSuccessful) {
                                Toast.makeText(baseContext, "회원가입 성공 이메일 확인", Toast.LENGTH_LONG).show()
                                changeVisibility("logout")
                            }
                            else { //sendEmailVerification 에서 이메일이 보내지지 않았을 경우
                                Toast.makeText(baseContext, "메일 전송 실패", Toast.LENGTH_LONG).show()
                                changeVisibility("logout")
                            }
                        }
                    }
                    else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                }
        }

        binding.loginBtn.setOnClickListener {
            // 이메일, 비밀번호 로그인
            val email:String = binding.authEmailEditView.text.toString()
            val password:String = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if(task.isSuccessful) {
                        if(MyApplication.checkAuth()) {
                            MyApplication.email = email
                            // changeVisibility("login")
                            finish()
                        }
                        else {
                            Toast.makeText(baseContext, "이메일 인증 실패", Toast.LENGTH_LONG).show()
                            changeVisibility("logout")
                        }
                    }
                    else {
                        Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_LONG).show()
                        changeVisibility("logout")
                    }
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                }
        }

        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            // 구글 로그인 처리 결과를 받아서 후속 처리
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            // ApiException : Google Play 서비스 호출이 실패 했을 때 태스크에서 반환할 예외
            try {
                // task에서 account에 대한 정보를 받음
                val account = task.getResult(ApiException::class.java)
                // account가 인증되었는지 확인하는 과정
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(credential)
                    .addOnCompleteListener(this){ task-> // 로그인이 완료되었을 때 후속 처리
                        if(task.isSuccessful){ // 인증이 성공적으로 이루어진 경우
                            MyApplication.email = account.email
                            // changeVisibility("login")
                            Log.d("mobileApp", "GoogleSignIn - Successful")
                            finish()
                        }
                        else{
                            changeVisibility("logout")
                            Log.d("mobileApp", "GoogleSignIn - Not Successful")
                        }
                    }
            } catch (e: ApiException) {
                changeVisibility("logout")
                // 토스트나 로그
                Log.d("mobileApp", "GoogleSignIn - ${e.message}")
            }
        }

        binding.logoutBtn.setOnClickListener {
            // 로그아웃
            MyApplication.auth.signOut()
            MyApplication.email = null
            // changeVisibility("logout")
            finish()
        }

        binding.googleLoginBtn.setOnClickListener {
            // 구글 로그인
            val gso : GoogleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val signInIntent : Intent = GoogleSignIn.getClient(this, gso).signInIntent
            requestLauncher.launch(signInIntent)
        }
    }

    fun changeVisibility(mode: String){
        if(mode.equals("signin")){
            binding.run {
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        }else if(mode.equals("login")){
            binding.run {
                authMainTextView.text = "${MyApplication.email} 님 반갑습니다."
                logoutBtn.visibility= View.VISIBLE
                goSignInBtn.visibility= View.GONE
                googleLoginBtn.visibility= View.GONE
                authEmailEditView.visibility= View.GONE
                authPasswordEditView.visibility= View.GONE
                signBtn.visibility= View.GONE
                loginBtn.visibility= View.GONE
            }
        }else if(mode.equals("logout")){
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }
        }
    }
}