//package com.hfad.myferma
//
//import android.animation.AnimatorSet
//import android.animation.ObjectAnimator
//import android.transition.AutoTransition
//import android.transition.Transition
//import android.transition.TransitionManager
//import android.view.View
//import androidx.annotation.DrawableRes
//import androidx.annotation.StringRes
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.constraintlayout.widget.ConstraintSet
//import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
//import java.util.concurrent.atomic.AtomicBoolean
//
//class FabIconAnimator constructor(private val container: ConstraintLayout) {
//    @DrawableRes
//    private var currentIcon: Int = 0
//
//    @StringRes
//    private var currentText: Int = 0
//    private var isAnimating: Boolean = false
//    private val button: ExtendedFloatingActionButton = container.findViewById(R.id.extended_fab)
//    private val listener: Transition.TransitionListener = object : Transition.TransitionListener {
//        public override fun onTransitionStart(transition: Transition) {
//            isAnimating = true
//        }
//
//        public override fun onTransitionEnd(transition: Transition) {
//            isAnimating = false
//        }
//
//        public override fun onTransitionCancel(transition: Transition) {
//            isAnimating = false
//        }
//
//        public override fun onTransitionPause(transition: Transition) {}
//        public override fun onTransitionResume(transition: Transition) {}
//    }
//
//    fun update(@DrawableRes icon: Int, @StringRes text: Int) {
//        val isSame: Boolean = currentIcon == icon && currentText == text
//        currentIcon = icon
//        currentText = text
//        animateChange(icon, text, isSame)
//    }
//
//    fun setOnClickListener(clickListener: View.OnClickListener?) {
//        if (clickListener == null) {
//            button.setOnClickListener(null)
//            return
//        }
//        val flag: AtomicBoolean = AtomicBoolean(true)
//        button.setOnClickListener(View.OnClickListener({ view: View? ->
//            if (!flag.getAndSet(false)) return@setOnClickListener
//            clickListener.onClick(view)
//            button.postDelayed(Runnable({ flag.set(true) }), 2000)
//        }))
//    }
//
//    // R.dimen.triple_and_half_margin is 56 dp.
//    private var isExtended: Boolean
//        private get() { // R.dimen.triple_and_half_margin is 56 dp.
//            return button.getLayoutParams().height != button.getResources()
//                .getDimensionPixelSize(R.dimen.activity_vertical_margin)
//        }
//        set(extended) {
//            setExtended(extended, false)
//        }
//
//    private fun animateChange(@DrawableRes icon: Int, @StringRes text: Int, isSame: Boolean) {
//        val extended: Boolean = isExtended
//        button.setText(text)
//        button.setIconResource(icon)
//        setExtended(extended, !isSame)
//        if (!extended) twitch()
//    }
//
//    private fun setExtended(extended: Boolean, force: Boolean) {
//        if (isAnimating || (extended && isExtended && !force)) return
//        val set: ConstraintSet = ConstraintSet()
//        set.clone(
//            container.getContext(),
//            if (extended) R.layout.fragment_finance else R.layout.fragment_add
//        )
//        TransitionManager.beginDelayedTransition(
//            container, AutoTransition()
//                .addListener(listener).setDuration(150)
//        )
//        if (extended) button.setText(currentText) else button.setText("")
//        set.applyTo(container)
//    }
//
//    private fun twitch() {
//        val set: AnimatorSet = AnimatorSet()
//        val twitchA: ObjectAnimator = animateProperty(
//            ROTATION_Y_PROPERTY,
//            TWITCH_START,
//            TWITCH_END
//        )
//        val twitchB: ObjectAnimator = animateProperty(
//            ROTATION_Y_PROPERTY,
//            TWITCH_END,
//            TWITCH_START
//        )
//        set.play(twitchB).after(twitchA)
//        set.start()
//    }
//
//    private fun animateProperty(property: String, start: Float, end: Float): ObjectAnimator {
//        return ObjectAnimator.ofFloat(container, property, start, end)
//            .setDuration(DURATION.toLong())
//    }
//
//    companion object {
//        private val ROTATION_Y_PROPERTY: String = "rotationY"
//        private val TWITCH_END: Float = 20f
//        private val TWITCH_START: Float = 0f
//        private val DURATION: Int = 200
//    }
//}