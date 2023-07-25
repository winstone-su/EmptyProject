package com.example.emptyproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.geometry.Point
import com.esri.arcgisruntime.geometry.SpatialReferences
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.BasemapStyle
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.ogc.wfs.WfsFeatureTable
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ArcGISRuntimeEnvironment.setApiKey("AAPKdd1a10f322d348fbaaa3c4694e10431d3_6yQyjAAdeVkRCFyquRj5YXPXMlUm-9xUD5uOjzmw0AZ51G6ccP4lMczoGZXoKt")

        mapView = findViewById(R.id.mapView)


        setupMap()

        addGraphic()

    }

    private fun setupMap() {

        val map = ArcGISMap(BasemapStyle.ARCGIS_STREETS)
        mapView.map = map


        // @Start(viewpoint)
        // set the viewpoint, Viewpoint(latitude, longitude, scale)
        mapView.setViewpoint(Viewpoint(34.0270, -118.8050, 72000.0))

    }

    private fun addGraphic() {
// create a graphics overlay and add it to the map view
        val graphicsOverlay = GraphicsOverlay()
        mapView.graphicsOverlays.add(graphicsOverlay)

        // create a point geometry with a location and spatial reference
        // Point(latitude, longitude, spatial reference)
        val point = Point(-118.8065, 34.0005, SpatialReferences.getWgs84())

        // create an opaque orange (0xFFFF5733) point symbol with a blue (0xFF0063FF) outline symbol
        val simpleMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, -0xa8cd, 10f)

        val blueOutlineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, -0xff9c01, 2f)
        simpleMarkerSymbol.outline = blueOutlineSymbol

        // create a graphic with the point geometry and symbol
        val pointGraphic = Graphic(point, simpleMarkerSymbol)

        // add the point graphic to the graphics overlay
        graphicsOverlay.graphics.add(pointGraphic)
    }
}