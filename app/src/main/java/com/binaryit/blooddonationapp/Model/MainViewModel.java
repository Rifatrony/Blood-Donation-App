package com.binaryit.blooddonationapp.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    MutableLiveData<UserModel> mutableLiveData = new MutableLiveData<>();

    public  void setText(UserModel s){
        mutableLiveData.setValue(s);
    }

    public MutableLiveData<UserModel> getText(){
        return mutableLiveData;
    }

}
