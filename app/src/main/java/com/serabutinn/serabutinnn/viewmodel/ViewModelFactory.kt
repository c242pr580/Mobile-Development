package com.serabutinn.serabutinnn.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serabutinn.serabutinnn.data.api.methods.Injection
import com.serabutinn.serabutinnn.repository.UserRepository
import com.serabutinn.serabutinnn.ui.DetailJobMitraViewModel
import com.serabutinn.serabutinnn.ui.customerpage.AddJobsViewModel
import com.serabutinn.serabutinnn.ui.customerpage.DetailJobCustomerViewModel
import com.serabutinn.serabutinnn.ui.customerpage.PaymentViewModel
import com.serabutinn.serabutinnn.ui.customerpage.home.HomeCustomerViewModel
import com.serabutinn.serabutinnn.ui.mitrapage.Profile.ProfileViewModel
import com.serabutinn.serabutinnn.ui.mitrapage.dashboard.DashboardViewModel
import com.serabutinn.serabutinnn.ui.mitrapage.home.HomeViewModel
import com.serabutinn.serabutinnn.ui.UpdateBioViewModel
import com.serabutinn.serabutinnn.ui.customerpage.UpdateJobViewModel

class ViewModelFactory (private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeCustomerViewModel::class.java) -> {
                HomeCustomerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddJobsViewModel::class.java) -> {
                AddJobsViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailJobCustomerViewModel::class.java) -> {
                DetailJobCustomerViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PaymentViewModel::class.java) -> {
                PaymentViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailJobMitraViewModel::class.java) -> {
                DetailJobMitraViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdateBioViewModel::class.java) -> {
                UpdateBioViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UpdateJobViewModel::class.java) -> {
                UpdateJobViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FaceCameraViewModel::class.java) -> {
                FaceCameraViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}