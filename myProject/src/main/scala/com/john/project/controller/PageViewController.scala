package com.john.project.controller

import com.john.project.service.PageViewService

class PageViewController {
  private val pageViewService = new PageViewService
  def getPV(dataPath: String)={
    pageViewService.getPV(dataPath)
  }
}
