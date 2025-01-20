package com.example.yourun.view.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
    private val onAnswerSelected: (Int, String) -> Unit
) : RecyclerView.Adapter<QuestionPagerAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
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

        fun bind(question: Question, onAnswerSelected: (Int, String) -> Unit) {

            questionText.text = question.text
            questionDetail.text = question.detail
            questionNumberCircle.text = question.id.toString()
            questionTotalText.text = "/${questions.size}"

            // 기존 버튼 초기화
            answersContainer.removeAllViews()

            var selectedButton: LinearLayout? = null
            var selectedImage: ImageView? = null //체크 이미지

            question.answers.forEach { answer ->
                val container = LinearLayout(itemView.context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        DimensionUtils.dpToPx(context, 69) // 답변 박스 고정
                    ).apply {
                        setMargins(
                            0,
                            DimensionUtils.dpToPx(context, 10),
                            0,
                            DimensionUtils.dpToPx(context, 10)
                        )
                    }
                    background = context.getDrawable(R.drawable.btn_question_background)
                    setPadding(0, 0, 0, 0) // 내부 패딩은 텍스트와 이미지에서 개별적으로 설정
                    isClickable = true
                    isFocusable = true
                }

                // 텍스트 추가
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
                    setImageResource(R.drawable.img_check) // Vector Drawable 참조
                    visibility = View.GONE // 초기에는 숨김
                }


                // 클릭 이벤트
                container.setOnClickListener {
                    selectedButton?.isSelected = false
                    selectedImage?.visibility = View.GONE // 이전 선택 이미지 숨기기
                    selectedButton = container
                    selectedImage = checkImage

                    container.isSelected = true
                    checkImage.visibility = View.VISIBLE // 현재 선택 이미지 표시

                    onAnswerSelected(question.id, answer)
                }

                // 뷰 추가
                container.addView(answerText)
                container.addView(checkImage)
                answersContainer.addView(container)
            }
        }
    }

}
