package com.doublej.jjfitnessapp.ui.models.exercises

data class FridayExercises(var nomeExercicio : String ?= null, var descricaoPrimaria :String ?= null, var descricaoSecundaria:String ?= null, var referenciaImagem: String ?= null, var dificuldade : Long ?= null, var seccao : String ?=null, var equipamento : ArrayList<String> ?= null, var favorito : Boolean ?= null, var id : Long ?= null)


