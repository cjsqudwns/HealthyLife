import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.healthylife.CalenderFragment
import com.example.healthylife.HomeFragment
import com.example.healthylife.UserFragment

class FragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> CalenderFragment()
            1 -> HomeFragment()
            2 -> UserFragment()
            else -> HomeFragment()
        }
    }
}