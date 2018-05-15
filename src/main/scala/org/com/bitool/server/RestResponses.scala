package org.com.bitool.server

/**
  * To be used for generic REST responses in the 2xx range.
  * See https://api-portal-np.monsanto.net/#/home/apistandards
  */
case class SuccessResponse(status: Int, message: String)

/**
  * To be used for generic REST responses in the 4xx or 5xx range
  * See https://api-portal-np.monsanto.net/#/home/apistandards
  */
case class ErrorResponse(status: Int, errorMessage: String)
