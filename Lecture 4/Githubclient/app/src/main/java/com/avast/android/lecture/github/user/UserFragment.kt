package com.avast.android.lecture.github.user

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.avast.android.lecture.github.R
import com.avast.android.lecture.github.data.GithubRepository
import com.avast.android.lecture.github.data.User
import com.avast.android.lecture.github.repository.Repository
import com.avast.android.lecture.github.repository.memory.InMemoryRepository
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_user.*

/**
 * Fragment to show user detail.
 */
class UserFragment : Fragment() {

    var user: User? = null
    lateinit var userRepositories: List<GithubRepository>

    lateinit var repositoryAdapter: RepositoryAdapter

    /**
     * Lazily initialize data repository.
     */
    private val dataRepository: Repository by lazy {
        InMemoryRepository()
    }

    /**
     * Inflate view hierarchy to this fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repositoryAdapter = RepositoryAdapter()
    }

    override fun onStart() {
        super.onStart()

        val username = arguments?.getString(KEY_USERNAME).orEmpty()

        // Load and show user detail
        user = dataRepository.getUser(username)
        user?.let { showUser(it) }

        // Load and show repositories
        userRepositories = dataRepository.getUserRepository(username)
//        sortRepositories(spinner_sorting.selectedItem as String)
    }

    private fun showUser(user: User) {
        with(user) {
            txt_username_value.text = login
            txt_url_value.text = html_url
            txt_followers_value.text = followers_url
            txt_repositories_value.text = url
            Glide.with(this@UserFragment).load(avatar_url).into(img_avatar)
        }
    }

    fun sortRepositories(sort: String) {
        Log.d(javaClass.simpleName, "Sorting by $sort")
        repositoryAdapter.repositories = when (sort) {
            getString(R.string.repository_sort_username) -> {
                userRepositories.sortedBy { githubRepository -> githubRepository.name }
            }
            getString(R.string.repository_sort_stars) -> {
                userRepositories.sortedBy { githubRepository -> githubRepository.stargazers_count }
            }
            else -> return
        }
    }

    /**
     * Stop any [Glide] operations related to this fragment.
     */
    override fun onStop() {
        super.onStop()
        Glide.with(this).onStop()
    }

    class DownloadAsyncTask: AsyncTask<String, Unit, User?>() {

        override fun doInBackground(vararg params: String?): User? {

            return null
        }

        override fun onPostExecute(result: User?) {
            super.onPostExecute(result)
            result?.let { user ->

            }
        }
    }

    class DownloadReposAsyncTask(private val repository: Repository,
                                 private val repositoryAdapter: RepositoryAdapter) : AsyncTask<String, Unit, List<GithubRepository>>() {

        override fun doInBackground(vararg params: String?): List<GithubRepository> {

            return emptyList()
        }

        override fun onPostExecute(result: List<GithubRepository>?) {
            super.onPostExecute(result)
            result?.let { repositories ->

            }
        }
    }

    companion object {

        val KEY_USERNAME = "username"

        /**
         * Factory method to create fragment instance. Framework requires empty default constructor.
         */
        @JvmStatic
        fun newInstance(username: String): UserFragment {
            val fragment = UserFragment()
            fragment.arguments = Bundle().apply {
                putString(KEY_USERNAME, username)
            }

            return fragment
        }
    }
}
