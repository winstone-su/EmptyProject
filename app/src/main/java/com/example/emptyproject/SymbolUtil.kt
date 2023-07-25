package com.example.emptyproject

import com.esri.arcgisruntime.symbology.CompositeSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol
import com.esri.arcgisruntime.symbology.TextSymbol

object SymbolUtil {

    fun buildCompositeSymbol(): CompositeSymbol {
        val compositeSymbol = CompositeSymbol()

        val simpleMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,0x458D30,20.0f)
        compositeSymbol.symbols.add(simpleMarkerSymbol)

        val textSymbol = TextSymbol(10.0f,"10",0xFFFFFF,TextSymbol.HorizontalAlignment.CENTER,TextSymbol.VerticalAlignment.MIDDLE)
        compositeSymbol.symbols.add(textSymbol)

        return  compositeSymbol

    }

}