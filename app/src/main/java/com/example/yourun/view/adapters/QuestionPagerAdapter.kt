package com.example.yourun.view.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.yourun.R
import com.example.yourun.model.data.Question
import com.example.yourun.model.repository.QuestionRepository.questions
import com.example.yourun.utils.DimensionUtils

class QuestionPagerAdapter(
    private val questions: List<Question>,
    private val onAnswerSelected: (Int, Int) -> Unit // 선택된 점수를 전달
) : RecyclerView.Adapter<QuestionPagerAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question, onAnswerSelected)
    }

    override fun getItemCount(): Int = questions.size

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.questionText)
        private val questionDetail: TextView = itemView.findViewById(R.id.questionDetail)
        private val answersContainer: LinearLayout = itemView.findViewById(R.id.answersContainer)
        private val questionNumberCircle: TextView = itemView.findViewById(R.id.questionNumberCircle)
        private val questionTotalText: TextView = itemView.findViewById(R.id.questionTotalText)

        fun bind(question: Question, onAnswerSelected: (Int, Int) -> Unit) {
            questionText.text = question.text
            questionDetail.text = question.detail
            questionNumberCircle.text = question.id.toString()
            questionTotalText.text = "/${questions.size}"

            answersContainer.removeAllViews()

            var selectedButton: LinearLayout? = null
            var selectedImage: ImageView? = null // 체크 이미지

            question.answers.forEachIndexed { index, answer ->
                val container = LinearLayout(itemView.context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        DimensionUtils.dpToPx(context, 69) // 답변 박스 높이 고정
                    ).apply {
                        setMargins(
                            0,
                            DimensionUtils.dpToPx(context, 10),
                            0,
                            DimensionUtils.dpToPx(context, 10)
                        )
                    }
                    background = context.getDrawable(R.drawable.btn_question_background)
                    setPadding(0, 0, 0, 0)
                    isClickable = true
                    isFocusable = true
                }

                val answerText = TextView(itemView.context).apply {
                    text = answer
                    textSize = 16f
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1f
                    ).apply {
                        marginStart = DimensionUtils.dpToPx(context, 20)
                    }
                    setPadding(
                        0,
                        DimensionUtils.dpToPx(context, 24),
                        0,
                        DimensionUtils.dpToPx(context, 24)
                    )
                    gravity = Gravity.CENTER_VERTICAL
                    setTextColor(context.getColor(android.R.color.black))
                }

                val checkImage = ImageView(itemView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        DimensionUtils.dpToPx(context, 32),
                        DimensionUtils.dpToPx(context, 32)
                    ).apply {
                        setMargins(
                            0,
                            DimensionUtils.dpToPx(context, 22),
                            DimensionUtils.dpToPx(context, 22),
                            DimensionUtils.dpToPx(context, 22)
                        )
                    }
                    setImageResource(R.drawable.img_check)
                    visibility = View.GONE
                }

                container.setOnClickListener {
                    selectedButton?.isSelected = false
                    selectedImage?.visibility = View.GONE
                    selectedButton = container
                    selectedImage = checkImage

                    container.isSelected = true
                    checkImage.visibility = View.VISIBLE

                    val score = if (index == 0) 1 else 2  // 1번 선택 시 +1, 2번 선택 시 +2
                    onAnswerSelected(question.id, score)
                }

                container.addView(answerText)
                container.addView(checkImage)
                answersContainer.addView(container)
            }
        }
    }
}
