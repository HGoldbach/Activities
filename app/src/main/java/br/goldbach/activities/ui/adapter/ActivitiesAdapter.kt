package br.goldbach.activities.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import br.goldbach.activities.R
import br.goldbach.activities.data.model.Activity
import br.goldbach.activities.data.model.ActivityStatus
import br.goldbach.activities.ui.fragment.AddActivityDialogFragment
import br.goldbach.activities.ui.viewmodel.HomeViewModel

class ActivitiesAdapter(
    private val homeViewModel: HomeViewModel,
    private val fragmentManager: FragmentManager,
    private val context: Context,
    private var activities: List<Activity> = listOf(),
    private var fragmentNumber: Int = 1
) : RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layout = when (fragmentNumber) {
            1 -> R.layout.created_activities_item
            2 -> R.layout.ongoing_activities_item
            3 -> R.layout.done_activities_item
            else -> 0
        }
        val inflater: View = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        holder.title.text = activity.title
        holder.description.text = activity.description

        holder.deleteBtn.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Activity")
                .setMessage("Do you really want to delete this activity?")
                .setPositiveButton("Yes") { _, _ ->
                    homeViewModel.deleteActivity(activity)
                }
                .setNegativeButton("Cancel") { _, _ ->
                    return@setNegativeButton
                }
                .create()
                .show()
        }

        if (fragmentNumber != 3) {
            holder.changeStatusBtn?.setOnClickListener {
                activity.status = when (fragmentNumber) {
                    1 -> ActivityStatus.ONGOING
                    else -> ActivityStatus.DONE
                }
                AlertDialog.Builder(context)
                    .setTitle("Change Status")
                    .setMessage("Do you really want to change the status of this activity?")
                    .setPositiveButton("Yes") { _, _ ->
                        homeViewModel.changeActivityStatus(activity)
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        return@setNegativeButton
                    }
                    .create()
                    .show()
            }
        }
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    fun update(it: List<Activity>, number: Int) {
        activities = it
        fragmentNumber = number
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.text_view_title)
        val description: TextView = itemView.findViewById(R.id.text_view_description)
        val deleteBtn: ImageView = itemView.findViewById(R.id.btn_delete)
        val changeStatusBtn: ImageView? = itemView.findViewById(R.id.btn_change_status)
    }

}