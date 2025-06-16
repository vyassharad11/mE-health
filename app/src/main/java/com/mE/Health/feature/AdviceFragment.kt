package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mE.Health.HomeActivity
import com.mE.Health.R
import com.mE.Health.databinding.AssistFragmentBinding
import com.mE.Health.databinding.AssistantFragmentBinding
import com.mE.Health.databinding.HomeFragmentBinding
import com.mE.Health.feature.adapter.AdviceListAdapter
import com.mE.Health.feature.adapter.AssistAdapter
import com.mE.Health.models.AdviceDTO

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class AdviceFragment : BaseFragment() {

    private lateinit var binding: AssistFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AssistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireContext() as HomeActivity).setBottomNavigationVisibility()
        initView()
        initHeader()
    }

    private fun initView(){
        binding.rvAssist.layoutManager = GridLayoutManager(requireActivity(), 2)
        var recyclerAdapter = AdviceListAdapter(requireActivity(),getDataList())
        binding.rvAssist.adapter = recyclerAdapter
    }

    private fun initHeader(){
        binding.toolbar.tvTitle.text = getString(R.string.advice)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getDataList():ArrayList<AdviceDTO> {
        return ArrayList<AdviceDTO>().apply {
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
            add(AdviceDTO("Finance", "1", R.drawable.ic_nav_menu_selected))
        }
    }
}