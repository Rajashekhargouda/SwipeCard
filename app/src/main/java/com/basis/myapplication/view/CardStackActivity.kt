package com.basis.myapplication.view


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.basis.myapplication.Constants
import com.basis.myapplication.R
import com.basis.myapplication.ViewModel.SwipeCardViewModel
import com.basis.myapplication.model.CardData
import com.basis.myapplication.util.Util
import com.basis.myapplication.util.hide
import com.basis.myapplication.util.show
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.activity_cardstack.*


class CardStackActivity:AppCompatActivity() {
    lateinit var cardList:ArrayList<CardData>
    lateinit var myAdapter: SwipeCardAdapter
    lateinit var tempList:ArrayList<CardData>
    lateinit var viewModel:SwipeCardViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardstack)
        viewModel = ViewModelProviders.of(this)[SwipeCardViewModel::class.java]
        subscribeCardLiveData()
        tempList = ArrayList()
        cardList = ArrayList()

        // refreshes as in all the cards appear again from the start
        img_refresh.setOnClickListener {
            cardList.clear()
            cardList.addAll(tempList)
            updateUI()
            img_refresh.visibility = View.GONE
        }

        fling_view.setFlingListener(object: SwipeFlingAdapterView.onFlingListener {
            override fun onScroll(p0: Float) {

            }

            override fun removeFirstObjectInAdapter() {
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
            }



            // used for going back to previous card
            override fun onLeftCardExit(p0: Any?) {
                val pos = tempList.indexOf(p0 as CardData)
                if (pos!=0){
                    val card = tempList[pos-1]
                    fling_view.setMaxVisible(cardList.size)
                    cardList.add(0,card)
                    status_pb.progress = tempList.size - cardList.size
                    txt_progress.text = ""+(tempList.size - cardList.size) + "/" +tempList.size
                }
                myAdapter.notifyDataSetChanged()
            }

            //used for swiping next card
            override fun onRightCardExit(p0: Any?) {
                cardList.removeAt(0)
                status_pb.progress = tempList.size - cardList.size
                txt_progress.text = ""+(tempList.size - cardList.size) + "/" +tempList.size
                myAdapter.notifyDataSetChanged()
                if (cardList.isEmpty())
                    img_refresh.visibility = View.VISIBLE
            }
        })

    }

    // observes data changes, any change in the data, consumes the changes
    private fun subscribeCardLiveData(){
        viewModel.cardLiveData.observe(this, Observer {
            when(it){
                is SwipeCardViewModel.CardResponse.Loading ->{
                    loading_pb.show()
                    txt_error.hide()
                }
                is SwipeCardViewModel.CardResponse.Success ->{
                    loading_pb.hide()
                    txt_error.hide()
                    cardList.clear()
                    cardList.addAll(it.cardList)
                    tempList.addAll(cardList)
                    updateUI()
                }
                is SwipeCardViewModel.CardResponse.Failure ->{
                    loading_pb.hide()
                    txt_error.show()
                    txt_error.text = getString(it.msgId)
                }
            }
        })
    }


    //Updates UI
    private fun updateUI(){
        status_pb.show()
        myAdapter = SwipeCardAdapter(cardList, this)
        fling_view.adapter = myAdapter
        myAdapter.notifyDataSetChanged()
        status_pb.max = cardList.size
        txt_progress.text = "0 / "+cardList.size
        status_pb.progress = 0
    }

    override fun onResume() {
        super.onResume()
        if (Util.isNetworkOnline(this))
            viewModel.getData(Constants.URL)
        else {
            txt_error.show()
            txt_error.text = getString(R.string.connect_to_internet)
        }
    }
}