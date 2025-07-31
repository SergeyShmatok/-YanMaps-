package ru.netology.yandexmaps.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.PlacemarkMapObject
import ru.netology.yandexmaps.R
import ru.netology.yandexmaps.room.db.entity.PointEntity
import ru.netology.yandexmaps.viewmodel.PointViewModel
import javax.inject.Inject


class Dialogs @Inject constructor(
    private val applicationContext: Context,
    private val viewModel: PointViewModel,
) {


    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(applicationContext, message, duration).show()

    }

    fun dialogForTap(
        mapObject: MapObject,
        fieldForDialog: View,
    ) {

        val message = "Что хотите сделать?"

        val dialogBuilder = AlertDialog.Builder(applicationContext)

        dialogBuilder.setMessage(message)
            .setCancelable(true) // Если установить значение false, то пользователь
//          не сможет закрыть диалоговое окно, например, нажатием в любой точке экрана.
//          В таком случае пользователь должен нажать одну из предоставленных опций.
            .setPositiveButton("Редактировать") { dialog, _ ->
                dialogForEdit(mapObject, fieldForDialog)
                dialog.cancel()
            }.setNegativeButton("Удалить") { dialog, _ ->
                val coordinates = (mapObject as PlacemarkMapObject).geometry
//                map.mapObjects.remove(mapObject)
                viewModel.removePoint(coordinates.latitude, coordinates.longitude)
                dialog.cancel()
            }

        val alert = dialogBuilder.create()

        alert.window?.setBackgroundDrawableResource(R.color.champagne)
//        alert.window?.setGravity(Gravity.BOTTOM) // - Чтобы двигать окно диалога
//        alert.window?.attributes?.x = 1500 // -//-
//        alert.window?.attributes?.y = -350 // -//-

        alert.setTitle("Изменение метки")

        alert.setIcon(R.drawable.edit_location_24)


        alert.show()

        val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)

        val paramsNegativeButt = negativeButton.layoutParams as MarginLayoutParams
        paramsNegativeButt.marginEnd = 20

        negativeButton.layoutParams = paramsNegativeButt

        negativeButton.setParameters(applicationContext)

        val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)

        val paramsPositiveButt = positiveButton.layoutParams as MarginLayoutParams

        positiveButton.layoutParams = paramsPositiveButt

        positiveButton.setParameters(applicationContext)
    }


    fun dialogForEdit(
        mapObject: MapObject,
        fieldForDialog: View,
    ) {

        val message = "Новое название:"

        val view =
            fieldForDialog.findViewById<TextInputLayout>(R.id.text_input_new_placemark).editText

        val dialogBuilder = AlertDialog.Builder(applicationContext)

        dialogBuilder.apply {
            setMessage(message)
            setCancelable(true)

            when (fieldForDialog.parent) { // Проверка, чтобы избежать ошибки с повторным
                // присоединением view к родителю.
                null -> setView(fieldForDialog)
                else -> {
                    (fieldForDialog.parent as ViewGroup).removeView(
                        fieldForDialog
                    )
                    setView(fieldForDialog)
                }
            }

            setPositiveButton("Ок") { dialog, _ ->
            }

        }

        val dialog = dialogBuilder.create()

        dialog.window?.setBackgroundDrawableResource(R.color.champagne)

        val text = "Редактирование"

        dialog.setTitle(text)

        dialog.setIcon(R.drawable.edit_location_24)

        dialog.show()

        view?.text?.clear()

        val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {

            val coordinates = (mapObject as PlacemarkMapObject).geometry
            val point = Point(coordinates.latitude, coordinates.longitude)

            when (view?.text.isNullOrBlank()) {
                true -> {
                    showToast(
                        "Поле логина или пароля не может быть пустым"
                    )
                }

                false -> {
                    viewModel.removePoint(
                        point.latitude,
                        point.longitude
                    )
                    viewModel.save(
                        PointEntity(
                            0,
                            point.latitude,
                            point.longitude,
                            view.text.toString()
                        )
                    )
                    dialog.cancel()

                }
            }
        }

        val paramsPositiveButt = positiveButton.layoutParams as MarginLayoutParams

        positiveButton.layoutParams = paramsPositiveButt

        positiveButton.setParameters(applicationContext)

    }


    fun dialogForCreate(
        point: Point,
        fieldForDialog: View,
    ) {

        val message = "Имя метки:"

        val view =
            fieldForDialog.findViewById<TextInputLayout>(R.id.text_input_new_placemark).editText

        val dialogBuilder = AlertDialog.Builder(applicationContext)

        dialogBuilder.apply {
            setMessage(message)
            setCancelable(true)

            when (fieldForDialog.parent) {
                null -> setView(fieldForDialog)
                else -> {
                    (fieldForDialog.parent as ViewGroup).removeView(
                        fieldForDialog
                    )
                    setView(fieldForDialog)
                }
            }

            setPositiveButton("Ок") { dialog, _ ->
            }

        }

        val dialog = dialogBuilder.create()

        dialog.window?.setBackgroundDrawableResource(R.color.champagne)

        val text = "Добавление метки"

        dialog.setTitle(text)

        dialog.setIcon(R.drawable.edit_location_24)

        dialog.show()

        view?.text?.clear()

        val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {

            when (view?.text.isNullOrBlank()) {
                true -> {
                    showToast(
                        "Поле логина или пароля не может быть пустым"
                    )
                }

                false -> {
                    viewModel.save(
                        PointEntity(
                            0,
                            point.latitude,
                            point.longitude,
                            view.text.toString()
                        )
                    )
                    dialog.cancel()

                }
            }
        }

        val paramsPositiveButt = positiveButton.layoutParams as MarginLayoutParams

        positiveButton.layoutParams = paramsPositiveButt

        positiveButton.setParameters(applicationContext)

    }

    fun Button.setParameters(context: Context) {
        this.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
        this.setTextColor(ContextCompat.getColor(context, R.color.stormy_sky))

    }

}