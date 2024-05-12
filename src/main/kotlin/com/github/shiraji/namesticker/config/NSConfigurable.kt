package com.github.shiraji.namesticker.config

import com.github.shiraji.namesticker.enums.ValidHorizontalAlignments
import com.github.shiraji.namesticker.enums.ValidVerticalAlignments
import com.github.shiraji.namesticker.enums.ValidForegroundColors
import com.github.shiraji.namesticker.properties.*
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBTextField
import javax.swing.*


class NSConfigurable(private val project: Project) : Configurable {
    private lateinit var root: JPanel
    private var disable: JCheckBox? = null
    private var fontSizeTextField: JBTextField? = null
    private var textTextField: JBTextField? = null
    private var horizontalAlignmentCombobox: JComboBox<ValidHorizontalAlignments>? = null
    private var verticalAlignmentCombobox: JComboBox<ValidVerticalAlignments>? = null
    private var foregroundColorCombobox: JComboBox<ValidForegroundColors>? = null
    private var resetDefaultButton: JButton? = null
    private var fontSizeErrorMessage: JLabel? = null

    inner class IntegerInputVerifier : InputVerifier() {
        override fun verify(c: JComponent): Boolean {
            val textField = c as JTextField
            return try {
                if (textField.text.isNotBlank()) {
                    val parsedValue = textField.text.toInt()
                    if (parsedValue < 0) {
                        fontSizeErrorMessage?.let {
                            it.foreground = JBColor.RED
                            it.text = "Font size must be a positive number"
                            it.isVisible = true
                            it.repaint()
                        }
                        return false
                    }
                }
                fontSizeErrorMessage?.isVisible = false
                fontSizeErrorMessage?.repaint()
                true
            } catch (e: NumberFormatException) {
                fontSizeErrorMessage?.let {
                    it.foreground = JBColor.RED
                    it.text = "Font size must be a positive number"
                    it.isVisible = true
                    it.repaint()
                }
                false
            }
        }
    }


    init {
        ApplicationManager.getApplication().runReadAction {
            setup()
        }
    }

    private fun setup() {
        horizontalAlignmentCombobox?.model = DefaultComboBoxModel(ValidHorizontalAlignments.values())
        verticalAlignmentCombobox?.model = DefaultComboBoxModel(ValidVerticalAlignments.values())
        foregroundColorCombobox?.model = DefaultComboBoxModel(ValidForegroundColors.values())
        resetDefaultButton?.addActionListener {
            resetToDefault()
        }

        fontSizeTextField?.inputVerifier = IntegerInputVerifier()
    }

    override fun createComponent(): JComponent {
        return root
    }

    override fun isModified(): Boolean {
        val config = PropertiesComponent.getInstance(project) ?: return false
        if (disable?.isSelected != config.isDisable()) return true
        if ((horizontalAlignmentCombobox?.selectedItem as? ValidHorizontalAlignments)?.name != config.horizontalAlignment()) return true
        if ((verticalAlignmentCombobox?.selectedItem as? ValidVerticalAlignments)?.name != config.verticalAlignment()) return true
        if ((foregroundColorCombobox?.selectedItem as? ValidForegroundColors)?.name != config.foregroundColor()) return true
        if (textTextField?.text != config.text()) return true

        val candidateFontSize = fontSizeTextField?.text?.toIntOrNull()
        // if no value is set and config has none default value, then it is modified
        if (candidateFontSize == null && config.fontSize() != DEFAULT_FONT_SIZE) return true
        // if value is set, and it is different from config value, then it is modified
        if (candidateFontSize != null && candidateFontSize != config.fontSize()) return true
        return false
    }

    override fun apply() {
        val config = PropertiesComponent.getInstance(project) ?: return
        config.setDisable(disable?.isSelected ?: false)
        config.setText(textTextField?.text ?: "")
        config.setFontSize(fontSizeTextField?.text?.toIntOrNull() ?: DEFAULT_FONT_SIZE)
        config.setHorizontalAlignment((horizontalAlignmentCombobox?.selectedItem as? ValidHorizontalAlignments)?.name ?: DEFAULT_HORIZONTAL_ALIGNMENT.name)
        config.setVerticalAlignment((verticalAlignmentCombobox?.selectedItem as? ValidVerticalAlignments)?.name ?: DEFAULT_VERTICAL_ALIGNMENT.name)
        config.setForegroundColor((foregroundColorCombobox?.selectedItem as? ValidForegroundColors)?.name ?: DEFAULT_FOREGROUND_COLOR.name)
    }

    override fun getDisplayName(): String {
        return "Name Sticker"
    }

    override fun reset() {
        super.reset()

        val config = PropertiesComponent.getInstance(project) ?: return
        disable?.isSelected = config.isDisable()
        horizontalAlignmentCombobox?.selectedItem = ValidHorizontalAlignments.valueOf(config.horizontalAlignment())
        verticalAlignmentCombobox?.selectedItem = ValidVerticalAlignments.valueOf(config.verticalAlignment())
        foregroundColorCombobox?.selectedItem = ValidForegroundColors.valueOf(config.foregroundColor())

        textTextField?.emptyText?.text = project.name
        val displayName = config.text()
        textTextField?.text = displayName.ifBlank { "" }

        fontSizeTextField?.emptyText?.text = DEFAULT_FONT_SIZE.toString()
        val fontSize = config.fontSize().toString()
        fontSizeTextField?.text = if (fontSize == DEFAULT_FONT_SIZE.toString()) "" else fontSize
    }

    private fun resetToDefault() {
        disable?.isSelected = DEFAULT_IS_DISABLE
        horizontalAlignmentCombobox?.selectedItem = DEFAULT_HORIZONTAL_ALIGNMENT
        verticalAlignmentCombobox?.selectedItem = DEFAULT_VERTICAL_ALIGNMENT
        foregroundColorCombobox?.selectedItem = DEFAULT_FOREGROUND_COLOR
        textTextField?.text = ""
        fontSizeTextField?.text = ""
    }
}
