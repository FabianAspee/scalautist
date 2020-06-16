package view.baseconfiguration.driver

import caseclass.CaseClassDB.Stipendio
import javafx.scene.control.{Label, ListView}
import junitparams.JUnitParamsRunner
import org.junit.{After, Before, Test}
import org.junit.runner.RunWith
import view.baseconfiguration.BaseTest
import view.driverviewoperations.StipendioDriver
import view.launchview.DriverLaunch

@RunWith(classOf[JUnitParamsRunner])
class StipendioTest extends BaseTest {
  var driverStipendio:StipendioDriver=_
  val TOTAL_STIPENDI = 4
  val NORMAL_DAY = "Giorni Normali : 0"
  val EXTRA_DAY = "Giorni Straordinari : 11"
  @Before
  def beforeEachFerieTest(): Unit = {

    setUp(classOf[DriverLaunch])
    driverStipendio = StipendioDriver(this)
  }
  @After
  def closeScene():Unit={
    closeCurrentWindow()
  }

  @Test
  def lengthListView():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    val list:ListView[Stipendio] = find("#salaryList")
    assert(list.getItems.parallelStream().count()==TOTAL_STIPENDI)
  }
  @Test
  def quantityNormalDayWork():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    driverStipendio.clickElementListView("2020-05-01")
    sleep(3000)
    val normalDay:Label = find("#normalDay")
    print(normalDay.getText)
    assert(normalDay.getText.equals(NORMAL_DAY))
  }
  @Test
  def quantityExtraDayWork():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    driverStipendio.clickElementListView("2020-05-01")
    sleep(3000)
    val extraDay:Label = find("#dayM")
    assert(extraDay.getText.equals(EXTRA_DAY))
  }
  @Test
  def notInfoForDate():Unit={
    driverStipendio.clickStipendioMenu()
    sleep(4000)
    driverStipendio.clickElementListView("2020-04-01")
    sleep(3000)
    val extraDay:Label = find("#dayM")
    assert(extraDay.getText.equals(EXTRA_DAY))
  }
}
