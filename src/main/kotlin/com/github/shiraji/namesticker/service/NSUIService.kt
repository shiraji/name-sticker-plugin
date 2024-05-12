package com.github.shiraji.namesticker.service

import com.github.shiraji.namesticker.properties.applyTo
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.IdeFrame
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import java.awt.Window
import java.awt.event.WindowFocusListener
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JRootPane
import javax.swing.SwingUtilities

class NSUIService(private val project: Project) : Disposable {
    private val frame: JFrame = WindowManager.getInstance().getFrame(project) ?: throw IllegalStateException()
    private val rootPane: JRootPane = UIUtil.findComponentOfType(frame.rootPane, JRootPane::class.java) ?: throw IllegalStateException()
    private val label: JLabel = JLabel(project.name)
    private val focusListeners: MutableList<WindowFocusListener> = mutableListOf()

    init {
        rootPane.add(label, BorderLayout.CENTER, 0)
        // For IDE that is not focused from the beginning
        if (!frame.isFocused) {
            prepareLabel()
            showLabel()
        }
    }

    fun prepareLabel() {
        label.setSize(rootPane.size.width, rootPane.size.height)
        PropertiesComponent.getInstance(project)?.applyTo(project, label)
    }

    fun isInSameFrame(opposite: Window?): Boolean {
        return SwingUtilities.getAncestorOfClass(IdeFrame::class.java, opposite) != frame
    }

    fun addWindowFocusListener(listener: WindowFocusListener) {
        frame.addWindowFocusListener(listener)
        focusListeners.add(listener)
    }

    fun showLabel() {
        label.isVisible = true
        label.repaint()
    }

    fun hideLabel() {
        label.isVisible = false
        label.repaint()
    }

    override fun dispose() {
        focusListeners.forEach {
            frame.removeWindowFocusListener(it)
        }
        rootPane.remove(label)
    }
}