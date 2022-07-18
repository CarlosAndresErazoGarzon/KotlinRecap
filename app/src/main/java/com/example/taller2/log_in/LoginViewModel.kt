package com.example.taller2.log_in

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.taller2.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginState = MutableLiveData<LoginState>(LoginState.Loading(""))
    val loginState : LiveData<LoginState> = _loginState

    fun validateUser(user:String, pass:String){

        val db = Room.databaseBuilder(
            getApplication<Application>().applicationContext,
            AppDatabase::class.java, "database-name"
        ).fallbackToDestructiveMigration().build()

        viewModelScope.launch {
            withContext(Dispatchers.Default){
                val compare = db.userDao().loadByIds(user)

                compare?.let{
                    if(compare.pass.equals(pass)){
                        _loginState.postValue(LoginState.Success(true))
                    }else{
                        _loginState.postValue(LoginState.Error(""))
                    }
                }?:run{
                    _loginState.postValue(LoginState.Error(""))
                }
            }
        }
    }
}

sealed class LoginState {
    data class Success(val movies: Boolean ) : LoginState()
    data class Error(val message: String) : LoginState()
    data class Loading(val message: String) : LoginState()
}