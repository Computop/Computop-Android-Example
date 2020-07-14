package com.computop.android.sdk.example.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.computop.android.sdk.PaymentMethod
import com.computop.android.sdk.example.R
import com.computop.android.sdk.example.adapters.PaymentMethodAdapter.ViewHolder
import kotlinx.android.synthetic.main.cell_payment.view.*

class PaymentMethodAdapter(private var onItemClicked: ((paymentMethod: PaymentMethod, paymentType: String) -> Unit)? = null) : RecyclerView.Adapter<ViewHolder>() {

    private var paymentMethods = mutableListOf<PaymentMethod>()

    override fun getItemCount(): Int = paymentMethods.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.cell_payment, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position in paymentMethods.indices) {
            holder.bind(paymentMethods[position])
        }
    }

    fun updateList(paymentMethods: MutableList<PaymentMethod>) {
        this.paymentMethods = paymentMethods
        notifyDataSetChanged()
    }

    class ViewHolder(private val paymentMethodView: View, private val onItemClicked: ((paymentMethod: PaymentMethod, paymentType: String) -> Unit)?) : RecyclerView.ViewHolder(paymentMethodView) {
        fun bind(paymentMethod: PaymentMethod) {
            paymentMethodView.paymentName.text = paymentMethod.localizedDescription
            paymentMethodView.paymentImage.setImageResource(paymentMethod.image)

            paymentMethodView.setOnClickListener(null)

            onItemClicked?.let { method ->
                paymentMethodView.setOnClickListener {
                    method.invoke(paymentMethod, paymentMethod.localizedDescription)
                }
            }
        }
    }
}