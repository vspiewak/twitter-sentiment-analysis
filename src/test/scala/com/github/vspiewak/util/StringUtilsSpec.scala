package com.github.vspiewak.util

import org.scalatest._

class StringUtilsSpec extends FunSpec with ShouldMatchers {

  describe("A String utility class") {

    it("should remove hashtags") {

        StringUtils.onlyWords("ok #word") should be ("ok")

    }

    it("should remove url") {

      StringUtils.onlyWords("ok http://google.com") should be ("ok")

    }

  }

}
