package com.example.golfbook.ui.chooseAvatar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.example.golfbook.R

class AvatarImageClickListener(
        private val context: Context,
        private val container: View,
        private val isMainImage: Boolean = false,
        private val setChangeAvatarEvent: (() -> Unit)?,
        private val fragment: ChooseAvatarFragment

) : View.OnClickListener{

    private val animatorSet = AnimatorSet()
    private val height: Int


    init {
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
    }

    override fun onClick(v: View?) {

        if (isMainImage) {

            makeAnimation()

        } else if (!fragment.backdropShown) {

            setChangeAvatarEvent?.invoke()
            makeAnimation()
        }

    }


    private fun makeAnimation() {

        fragment.backdropShown = !fragment.backdropShown

        // Cancel the existing animations
        animatorSet.removeAllListeners()
        animatorSet.end()
        animatorSet.cancel()

        val translateY = -(context.resources.getDimensionPixelSize(R.dimen.avatar_images_available_reveal_height))

        val animator = ObjectAnimator.ofFloat(container, "translationY", (if (fragment.backdropShown) 0 else translateY).toFloat())

        animator.duration = 500

        animator.interpolator = AccelerateDecelerateInterpolator()

        animatorSet.play(animator)
        animator.start()
    }
}