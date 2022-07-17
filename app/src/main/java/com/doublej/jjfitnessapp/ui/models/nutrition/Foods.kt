package com.doublej.jjfitnessapp.ui.models.nutrition

data class Foods(var nomePrato : String ?= null, var ReferenciaImagem :String ?= null, var Calorias:Long ?= null, var Carbohidratos: Long ?= null, var Proteinas : Long ?= null, var Gorduras : Long ?=null, var Dieta : String ?= null, var Refeicao : ArrayList<String> ?= null , var id : Long ?= null)


