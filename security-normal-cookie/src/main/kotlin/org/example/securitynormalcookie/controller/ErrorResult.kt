package org.example.securitynormalcookie.controller

data class ErrorResult(val code: String, val message: String?, val validation: Map<String, String>? = null)