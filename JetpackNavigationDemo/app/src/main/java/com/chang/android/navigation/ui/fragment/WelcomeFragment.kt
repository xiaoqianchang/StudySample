package com.chang.android.navigation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.chang.android.jetpacknavigationdemo.R

/**
 * Description: 启动页
 * <p>
 * Created by Chang.Xiao on 2021/3/11 4:57 PM.
 *
 * @version 1.0
 */
class WelcomeFragment : Fragment(), View.OnClickListener {

    lateinit var btnLogin: Button
    lateinit var btnRegister: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi(view)
    }

    private fun initUi(view: View) {
        btnLogin = view.findViewById(R.id.btn_login)
        btnRegister = view.findViewById(R.id.btn_register)

        btnLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

            }

        })
//        btnLogin.let {  }
        btnLogin.setOnClickListener {
            val navOption = navOptions {
                anim {
                    enter = R.anim.slide_in_right
                    exit = R.anim.slide_out_left
                    popEnter = R.anim.slide_in_left
                    popExit = R.anim.slide_out_right
                }
            }
            val bundle = Bundle()
            bundle.putString("name", "Open")
            findNavController().navigate(R.id.loginFragment, bundle, navOption)
        }

        btnLogin.setOnClickListener {
//            val action = Welcomefrag
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WelcomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WelcomeFragment().apply {
                arguments = Bundle().apply {
                    putString("ARG_PARAM1", param1)
                    putString("ARG_PARAM2", param2)
                }
            }
    }

    override fun onClick(v: View?) {


    }
}