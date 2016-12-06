package com.jm

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.jm.activity.BaseActivity
import com.jm.activity.InfoActivity
import com.jm.activity.LoginActivity
import com.jm.data.utils.getShapeDrawable
import com.jm.fragment.BaseFragment
import com.jm.fragment.ChatFragment
import com.jm.fragment.HomeFragment
import org.jetbrains.anko.*

class MainActivity : BaseActivity(){
    var main_tab: BottomNavigationView? = null
    var prevMenuItem:MenuItem?=null
    var recycle:RecyclerView?=null
    var rAdapter:MyRecycleAdapter?=null
    var HomeFragment=HomeFragment::class.java.newInstance()
    var ChatFragment=ChatFragment::class.java.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.jm_main)
        main_tab = findViewById(R.id.main_tab) as BottomNavigationView?

        main_tab?.setOnNavigationItemSelectedListener {item: MenuItem->
            prevMenuItem=item
            when(item.itemId){
                R.id.home->{
                    setlectFragment(HomeFragment,ChatFragment)
                }
                R.id.chat->{
                    setlectFragment(ChatFragment,HomeFragment)
                }
            }
            true }
        main_tab?.clearDisappearingChildren()
        main_tab?.clearFocus()
        /**
         * 左侧视图创建
         * */
        initleftView()
        setDefaultFragment()
    }

    fun setlectFragment(fraDown:Fragment,fraUp:Fragment){
        val newft = supportFragmentManager.beginTransaction()
        if(fraUp.isAdded){
            newft.remove(fraUp)
        }
        if(!fraDown.isAdded){
            newft.add(R.id.main_container,fraDown)
        }
        newft.commit()
    }
    /**
     * 设置默认的
     */
     fun setDefaultFragment() {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        transaction.replace(R.id.main_container, HomeFragment)
        transaction.commit()
    }

    private fun initleftView() {
        //logout
        findViewById(R.id.main_left_logout).apply {
            background= getShapeDrawable(Color.parseColor("#ec407a"),5f,1,Color.parseColor("#ec407a"),null,null)
            setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()

            }
        }
        //recycleview
        rAdapter= MyRecycleAdapter(this@MainActivity)
        recycle=findViewById(R.id.main_left_recycle) as RecyclerView
        recycle?.apply {
            layoutManager=LinearLayoutManager(this@MainActivity)
            adapter=rAdapter
        }
    }

    /**
     * RecycleViewAdapter
     * */

    class MyRecycleAdapter(context:Context): RecyclerView.Adapter<MyRecycleAdapter.MyHolder>() {
        var mCt=context
        var leftDataList= arrayListOf(
                leftData("个人信息",R.mipmap.ic_launcher),
                leftData("会员充值",R.mipmap.ic_launcher),
                leftData("我的邮箱",R.mipmap.ic_launcher),
                leftData("立即更新",R.mipmap.ic_launcher)
        )
        companion object {
            val IMAGE = 0x36f01
            val TEXT = 0x36f02
        }
        override fun onBindViewHolder(holder: MyHolder?, position: Int) {
            holder?.image?.backgroundResource=leftDataList[position].pic!!
            holder?.txt?.text=leftDataList[position].name!!
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
             val rootView=layout().createView(AnkoContext.Companion.create(mCt))
            return MyHolder(rootView)
        }

        override fun getItemCount(): Int {
            return leftDataList.size
        }

        inner class MyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
            var image:ImageView?=null
            var txt:TextView?=null
            init {
                image=itemView?.find<ImageView>(IMAGE)
                txt=itemView?.find<TextView>(TEXT)
            }

        }
        inner class layout : AnkoComponent<Context> {
            override fun createView(ui: AnkoContext<Context>)= with(ui) {
                linearLayout {
                    lparams {
                        width= org.jetbrains.anko.matchParent
                        height= dip(50)
                        orientation=LinearLayout.HORIZONTAL
                    }

                    leftPadding=dip(20)
                    rightPadding=dip(20)
                    gravity=Gravity.CENTER
                    linearLayout {
                        lparams {
                            width= org.jetbrains.anko.wrapContent
                            height= org.jetbrains.anko.wrapContent
                            orientation=LinearLayout.HORIZONTAL
                        }
                        imageView {
                            id=IMAGE
                            lparams {
                                width= dip(30)
                                height= dip(30)
                            }
                        }
                        textView {
                            id=TEXT
                            lparams {
                                width= org.jetbrains.anko.wrapContent
                                height= org.jetbrains.anko.matchParent
                                leftMargin=dip(40)
                            }
                            textSize=12f
                            gravity=Gravity.CENTER_VERTICAL

                        }
                    }
                }
            }
        }

    }
    data class leftData(var name:String?=null,var pic:Int?=null,var imgUrl:String?=null){}
}

