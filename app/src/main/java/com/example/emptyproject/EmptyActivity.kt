package com.example.emptyproject

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.data.FeatureTable
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.data.ServiceFeatureTable
import com.esri.arcgisruntime.geometry.GeometryType
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.loadable.LoadStatus
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.ogc.wfs.OgcAxisOrder
import com.esri.arcgisruntime.ogc.wfs.WfsFeatureTable
import com.esri.arcgisruntime.ogc.wfs.WfsService
import com.esri.arcgisruntime.symbology.Renderer
import com.esri.arcgisruntime.symbology.SimpleFillSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer
import com.esri.arcgisruntime.symbology.Symbol
import com.esri.arcgisruntime.symbology.UniqueValueRenderer
import com.example.emptyproject.databinding.ActivityEmptyBinding
import java.util.Random


class EmptyActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "EmptyActivity"
    }

    private val binding: ActivityEmptyBinding by lazy { ActivityEmptyBinding.inflate(layoutInflater) }

    private val mapView: MapView by lazy { binding.mapView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ArcGISRuntimeEnvironment.setApiKey("AAPKdd1a10f322d348fbaaa3c4694e10431d3_6yQyjAAdeVkRCFyquRj5YXPXMlUm-9xUD5uOjzmw0AZ51G6ccP4lMczoGZXoKt")
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud9978704000,none,TRB3LNBHPFMB4P7EJ046")

        val map = ArcGISMap(BasemapStyle.ARCGIS_STREETS)
        mapView.map = map

        mapView.setViewpoint(Viewpoint(25.96213,119.689064,5000.0))


        val hostUrl = "http://47.114.126.208:5431/geoserver/tamp/ows?service=WFS&version=2.0.0&request=GetCapabilities"

        val tableName = "tamp:pt_tamp_stake"

        val wfsService = WfsService(hostUrl)
        wfsService.addDoneLoadingListener {
            if (wfsService.loadStatus == LoadStatus.FAILED_TO_LOAD) {
                Log.e(TAG, "wfsService加载失败: ")
            }else {
                val layerInfos = wfsService.serviceInfo.layerInfos
                for (info in layerInfos) {
                    Log.e(TAG, "加载wfsService Name: ${info.name}")
                }
                val wfsLayerInfo = layerInfos[layerInfos.size - 1]
                val featureTable = WfsFeatureTable(wfsLayerInfo)
                featureTable.axisOrder = OgcAxisOrder.NO_SWAP
                featureTable.featureRequestMode = ServiceFeatureTable.FeatureRequestMode.MANUAL_CACHE

                val featureLayer = FeatureLayer(featureTable)
                featureTable.addDoneLoadingListener {
                    val renderer = featureLayer.renderer
                    if (renderer is SimpleRenderer) {
                        Log.e(TAG, "onCreate: SimpleRenderer", )
                    }else if (renderer is UniqueValueRenderer) {
                        Log.e(TAG, "onCreate: UniqueValueRenderer", )
                    }
                    // 获取 SimpleRenderer 的符号
                    val simpleRenderer = featureLayer.renderer as SimpleRenderer
                    val defaultSymbol: Symbol = simpleRenderer.symbol

// 创建一个新的 UniqueValueRenderer 对象，并设置默认符号
                    val uniqueValueRenderer = UniqueValueRenderer()
                    uniqueValueRenderer.defaultSymbol = defaultSymbol

// 假设您希望根据名为 "category" 的字段值来区分不同的符号样式
                    val targetField = "category"

// 假设您有不同的字段值，比如 "value1", "value2", "value3" 等等
                    val uniqueValues = arrayOf("value1", "value2", "value3")

// 为每个唯一值创建一个 UniqueValueInfo 并设置相应的符号样式
                    for (value in uniqueValues) {
                        // 创建新的符号（例如，简单点符号、线符号或面符号）
                        val newMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.RED, 10.0f)
                        // 创建一个 UniqueValueInfo 并将其添加到 UniqueValueRenderer 中
//                        uniqueValueRenderer.uniqueValues.add(UniqueValueInfo(value, targetField, newMarkerSymbol))
                    }

// 将 UniqueValueRenderer 设置为 FeatureLayer 的 Renderer
                    featureLayer.renderer = uniqueValueRenderer

                    if (featureLayer.renderer is SimpleRenderer) {
                        Log.e(TAG, "onCreate: SimpleRenderer", )
                    }else if (featureLayer.renderer is UniqueValueRenderer) {
                        Log.e(TAG, "onCreate: UniqueValueRenderer", )
                    }

// 重新绘制图层以更新样式
//                    featureLayer.resetRenderer()
                    featureLayer.renderer = getRandomRendererForTable(featureTable)

                }

                mapView.map.operationalLayers.add(featureLayer)
                // populate the table
                val featureQueryResultFuture = featureTable
                    .populateFromServiceAsync(QueryParameters(), false, null)
                // run when the table has been populated
                featureQueryResultFuture.addDoneListener {

                    // zoom to the extent of the layer
                    mapView.setViewpointGeometryAsync(featureLayer.fullExtent, 50.0)
                }
            }
        }
        wfsService.loadAsync()



    }

    private fun getRandomRendererForTable(table: FeatureTable): Renderer {
        return when (table.geometryType) {
            GeometryType.POINT, GeometryType.MULTIPOINT -> {
                SimpleRenderer(
                    SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, getRandomColor(), 20f)
                )
            }
            GeometryType.POLYGON, GeometryType.ENVELOPE -> {
                SimpleRenderer(SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, getRandomColor(), null))
            }
            else -> {
                SimpleRenderer(SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, getRandomColor(), 1f))
            }
        }
    }

    private fun getRandomColor(): Int {
        val random = Random()
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

}