package com.basis.myapplication.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.basis.myapplication.R
import com.basis.myapplication.model.CardData
import com.basis.myapplication.repository.NetworkRepo
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class SwipeCardViewModel:ViewModel() {
    var cardLiveData = MutableLiveData<CardResponse>()


    //Live data is used to set the status of the response
    //Ex: Success, Fail or Loading
    //CardLiveData is being observed in MainActivity
    // uses coroutines for asynchronous operation
    fun getData(url:String){
        cardLiveData.value = CardResponse.Loading
        launch(UI) {
            cardLiveData.value = async(CommonPool) {
                getDataFromServer(url)
            }.await()
        }
    }


    private fun getDataFromServer(url: String):CardResponse{
        val networkRepo = NetworkRepo()
        val result = networkRepo.getData(url)
        if (result!=null){
            return CardResponse.Success(result)
        }
        return CardResponse.Failure(R.string.something_went_wrong)
    }



    // used as STATE for the live data
    sealed class CardResponse{
        data class Success(var cardList:ArrayList<CardData>):CardResponse()
        data class Failure(var msgId:Int):CardResponse()
        object Loading:CardResponse()
    }
}