package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.MeUserSecondFragmentBinding
import com.mE.Health.feature.adapter.RadioButtonListAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MeUserSecondFragment : BaseFragment() {

    private lateinit var binding: MeUserSecondFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MeUserSecondFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        binding.rvRadioButton.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = RadioButtonListAdapter(requireActivity(),getDataList())
        binding.rvRadioButton.adapter = adapter
        adapter.apply {
            onItemClickListener =  object : RadioButtonListAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    selectedItem = position
                    notifyDataSetChanged()
                }
            }
        }

        binding.continueLayout.rllContinue.setOnClickListener {
            addFragment(
                R.id.fragment_container,
                MeUserWelcomeFragment(),
                "MeUserWelcomeFragment",
                "MeUserSecondFragment"
            )
        }
    }

    private fun getDataList():ArrayList<String> {
        return ArrayList<String>().apply {
            add(getString(R.string.from_friend_family))
            add(getString(R.string.social_media))
            add(getString(R.string.online_advertisement))
            add(getString(R.string.search_engine))
        }
    }
}