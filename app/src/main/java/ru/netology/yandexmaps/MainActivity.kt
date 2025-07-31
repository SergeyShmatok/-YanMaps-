package ru.netology.yandexmaps

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.netology.yandexmaps.adapter.OnInteractionListener
import ru.netology.yandexmaps.adapter.PointAdapter
import ru.netology.yandexmaps.databinding.ActivityAppBinding
import ru.netology.yandexmaps.dialogs.Dialogs
import ru.netology.yandexmaps.room.db.entity.PointEntity
import ru.netology.yandexmaps.viewmodel.PointViewModel


@AndroidEntryPoint
class MainActivity(

) : AppCompatActivity() {

    companion object {
        private val POINT1 = Point(59.9386, 30.3141)
        private val POSITION = CameraPosition(POINT1, 6.5f, 0.0f, 30.0f)
    }

    private val viewModel by viewModels<PointViewModel>()

    private lateinit var mapView: MapView

    private lateinit var inputFieldForDialog: View // Расширение окна диалога

    // "Сильные" ссылки👇

    private lateinit var imageProvider: ImageProvider

    private lateinit var placemarkTapListener: MapObjectTapListener

    private lateinit var inputListener: InputListener

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(BuildConfig.MAPS_API_KEY)

        MapKitFactory.initialize(this)

        val binding = ActivityAppBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.listPointsButton.setOnClickListener {

            binding.listBox.apply {
                visibility = if (isVisible) View.GONE
                else View.VISIBLE
            }

        }

        inputFieldForDialog = layoutInflater.inflate(R.layout.input_field_for_dialog, null)


        mapView = findViewById(R.id.mapview)

        val map = mapView.mapWindow.map

        map.move(POSITION)

        val adapter = PointAdapter(object : OnInteractionListener {

            override fun onTap(point: PointEntity) {
                val point = Point(point.latitude, point.longitude)
                val position = CameraPosition(point, 10.0f, 0.0f, 30.0f)
                map.move(position)
                binding.listBox.visibility = View.GONE
            }
        })

        binding.list.adapter = adapter

        val dialog = Dialogs(this, viewModel)

        placemarkTapListener = MapObjectTapListener { mapObject, _ ->
            dialog.dialogForTap(
                mapObject,
                inputFieldForDialog,
            )
            true
        } // Слушатель тапа по точке.
        // 💡Можно сделать разные категории TapListener'ов для разных точек и добавлять их по ситуации.

        fun initInputListener(): InputListener {

            inputListener = object : InputListener {
                override fun onMapTap(map: Map, point: Point) {
                    dialog.dialogForCreate(point, inputFieldForDialog)
                    true
                }

                override fun onMapLongTap(map: Map, point: Point) {}
            }
            return inputListener
        }


        imageProvider = ImageProvider.fromResource(this, R.drawable.ic_location_4)


        map.addInputListener(initInputListener())

        binding.buttonPlus.setOnClickListener {

            val currentPoint = mapView.mapWindow.map.cameraPosition.target
            val zoom = mapView.mapWindow.map.cameraPosition.zoom
            map.move(
                CameraPosition(currentPoint, zoom + 1.0f, 0.0f, 30.0f),
                Animation(Animation.Type.LINEAR, 0.5f), null
            )

        }

        binding.buttonMinus.setOnClickListener {
            val currentPoint = mapView.mapWindow.map.cameraPosition.target
            val zoom = mapView.mapWindow.map.cameraPosition.zoom
            map.move(
                CameraPosition(currentPoint, zoom - 1.0f, 0.0f, 30.0f),
                Animation(Animation.Type.LINEAR, 0.5f), null
            )

        }


        viewModel.pointsData.flowWithLifecycle(lifecycle).onEach { pointData ->

            adapter.submitList(pointData)

            map.mapObjects.clear()

            pointData.forEach { pointEntity ->
                map.mapObjects.addPlacemark().apply {
                    geometry = Point(pointEntity.latitude, pointEntity.longitude)
                    setText(pointEntity.text, TextStyle().apply {
                        size = 10F
                        placement = TextStyle.Placement.BOTTOM
                    })
                    setIcon(imageProvider)
                    addTapListener(placemarkTapListener)
                }
            }
        }.launchIn(lifecycleScope)
    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

}
