package com.computop.android.sdk.example.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.computop.android.sdk.example.Article
import com.computop.android.sdk.example.R
import kotlinx.android.synthetic.main.cell_article.view.*

class ArticleAdapter(private var onItemClicked: ((article: Article) -> Unit)? = null) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private var articles = mutableListOf<Article>()

    override fun getItemCount(): Int = articles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.cell_article, parent, false)
        return ViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position in articles.indices) {
            holder.bind(articles[position])
        }
    }

    fun updateList(articles: MutableList<Article>) {
        this.articles = articles
        notifyDataSetChanged()
    }

    class ViewHolder(private val articleView: View, private val onItemClicked: ((article: Article) -> Unit)?) : RecyclerView.ViewHolder(articleView) {
        fun bind(article: Article) {
            articleView.name.text = article.name
            articleView.color.text = article.color
            articleView.price.text = "Price: ${article.price}$"
            articleView.image.setImageResource(article.image)
            articleView.add.setOnClickListener(null)

            articleView.add.setOnClickListener(null)
            onItemClicked?.let { method ->
                articleView.add.setOnClickListener { method.invoke(article) }
            }
        }
    }
}