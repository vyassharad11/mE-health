package com.mE.Health.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.mE.Health.R
import com.mE.Health.databinding.AdviceListFragmentBinding
import com.mE.Health.feature.adapter.AdviceAdapter
import com.mE.Health.utility.AdviceFilterItem
import com.mE.Health.utility.BottomSheetAdviceFilter
import com.mE.Health.utility.BottomSheetFilter
import com.mE.Health.utility.FilterItem
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class AdviceListFragment : BaseFragment() {

    private lateinit var binding: AdviceListFragmentBinding
    private var filterList = ArrayList<AdviceFilterItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AdviceListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomNavigationVisibility(requireActivity())
        initView()
        initHeader()
    }

    private fun initView() {
        binding.rvAdvice.layoutManager = LinearLayoutManager(requireActivity())
        val listAdapter = AdviceAdapter(requireActivity())
        binding.rvAdvice.adapter = listAdapter
        listAdapter.apply {
            onItemClickListener = object : AdviceAdapter.OnClickCallback {
                override fun onClicked(view: View?, position: Int) {
                    openReadMoreDialog(
                        requireActivity(),
                        "",
                        getString(R.string.appointment_description)
                    )
                }
            }
        }
    }


    private fun initHeader() {
        binding.toolbar.tvTitle.text = getString(R.string.back)
        binding.toolbar.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.toolbar.ivSetting.visibility = View.VISIBLE
        binding.toolbar.ivSetting.setImageResource(R.drawable.ic_filter_primary)

        filterList = getFilterList()
        binding.toolbar.ivSetting.setOnClickListener {
            val bottomSheet = BottomSheetAdviceFilter(filterList)
            bottomSheet.setOnCompleteListener(object : BottomSheetAdviceFilter.OnCompleteListener {
                override fun onComplete(filterItem: ArrayList<AdviceFilterItem>) {
//                    var filterItemList: ArrayList<String> = ArrayList()
//                    for (item in filterItem) if (item.isChecked) filterItemList.add(item.name)
                }
            })
            bottomSheet.show(
                requireActivity().supportFragmentManager,
                "BottomSheetFilter"
            )
        }
    }

    private fun getFilterList(): ArrayList<AdviceFilterItem> {
        filterList = ArrayList()
        filterList.apply {
            add(AdviceFilterItem("All", R.drawable.ic_filter_orange, false))
            add(AdviceFilterItem("Fav", R.drawable.ic_like_orange, false))
            add(AdviceFilterItem("Ignore", R.drawable.ic_ignore_orange, false))
            add(AdviceFilterItem("Review", R.drawable.ic_review_orange, false))
            add(AdviceFilterItem("Read", R.drawable.ic_read_orange, false))
            add(AdviceFilterItem("Unread", R.drawable.ic_unread_orange, false))
        }
        return filterList
    }
}