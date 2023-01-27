import org.apache.http.client.utils.URIBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DesiDimeCopier {
    public WebDriver driver = null;
    public JavascriptExecutor js = null;
    public WebDriverWait wait = null;
    final String desiDimeURL = "https://www.desidime.com/new";
    final String xpath = "xpath";
    final String css = "css";
    final String id = "id";
    final String name = "name";
    final String className = "className";
    final String lightningDeal = "LightningDeal";
    final String specialDealKey = "specialDealKey";
    static ArrayList<String> postsList = new ArrayList<>();
    String previousFirstDealTitle = "[58% off] Syska Ionic Function HD 3600I Hair Dryer and Ionic HS 2021I Hair St...";
    final String newPostUrl = "https://nonstopdeals.in/wp-admin/post-new.php";
    String firstAmazonDealTitle = "";
    final String telegramSendURL = "https://jobsinsider.in/wp-admin/admin.php?page=telegram_send";
    final boolean sendToTelegram = false;

    public static final List<String> amazonAffialteIdList = new ArrayList<>();
    public static final List<String> flipkartAffialteIdList = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(DesiDimeCopier.class);

    static {
        amazonAffialteIdList.add("dealsalert13-21");
        flipkartAffialteIdList.add("vishalkum20");	}

    @Test
    public void f() {
        try {
            ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(0));
            driver.get(desiDimeURL);
            //ArrayList<WebElement> allDeals = (ArrayList<WebElement>) js
            //		.executeScript("return document.getElementsByClassName('deal-box shadow');");
            ArrayList<WebElement> allDeals = (ArrayList<WebElement>) js
                    .executeScript("return document.getElementsByClassName('l-deal-box');");
            ArrayList<WebElement> amazonDeals = new ArrayList<>();
            ArrayList<WebElement> flipkartDeals = new ArrayList<>();
            for (WebElement allDeal : allDeals) {
                if (allDeal.getText().contains("Amazon")) {
                    amazonDeals.add(allDeal);
                } else if (allDeal.getText().contains("Flipkart")) {
                    flipkartDeals.add(allDeal);
                }
            }

            if (!amazonDeals.isEmpty()) {
                try {
                    for (WebElement amazonDeal : amazonDeals) {

                        if (!(postsList.contains(
                                amazonDeal.findElements(By.className("l-deal-dsp")).get(0).getText()))) {
                            postsList.add(amazonDeal.findElements(By.className("l-deal-dsp")).get(0).getText());
                            WebElement link = amazonDeal.findElements(By.className("l-deal-dsp")).get(0).findElements(By.tagName("a")).get(0);
                            postDesiDimeDeal(link);
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
            if (!flipkartDeals.isEmpty()) {
                try {
                    for (WebElement flipkartDeal : flipkartDeals) {

                        if (!(postsList.contains(
                                flipkartDeal.findElements(By.className("l-deal-dsp")).get(0).getText()))) {
                            postsList.add(flipkartDeal.findElements(By.className("l-deal-dsp")).get(0).getText());
                            WebElement link =  flipkartDeal.findElements(By.className("l-deal-dsp")).get(0).findElements(By.tagName("a")).get(0);
                            postDesiDimeDeal(link);
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
                    //throw e;
                }
            }
            try {
                Thread.sleep(30000);
            } catch (Exception e) {
                // TODO: handle exception
            }
            f();
        } catch (Exception e) {
            //e.printStackTrace();
            f();
        }
    }

    public void postDesiDimeDeal(WebElement desiDimeLink) throws Exception {
        Actions actionOpenLinkInNewTab = new Actions(driver);
        actionOpenLinkInNewTab.moveToElement(desiDimeLink).keyDown(Keys.CONTROL) // MacOS: Keys.COMMAND
                .keyDown(Keys.SHIFT).click(desiDimeLink).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).perform();

        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        String postTitle = null;
        String amazonLinkHref;
        try {
            driver.switchTo().window(tabs.get(1));
            /*
             * postTitle = driver.findElement(By.cssSelector(
             * "#deal-detail-like-dislike-container > div > div.grid-100.tablet-grid-100.grid-parent.gutter-bottom > div.grid-45.tablet-grid-45.grid-parent > h1"
             * )) .getText();
             */
            WebElement amazonLink = driver.findElement(By.cssSelector(
                    "#deal-detail-like-dislike-container > div:nth-child(1) > div.cf > div.grid-80.tablet-grid-80.grid-parent.pl30.pr20 > div.grid-100.tablet-grid-100.grid-parent > div:nth-child(2) > a"));
            amazonLinkHref = amazonLink.getAttribute("href");
            if (amazonLinkHref.contains("amazon") || amazonLinkHref.contains("Amazon")) {
                postDealWrapper(amazonLink.getAttribute("href"));
            } else if (amazonLinkHref.contains("flipkart") || amazonLinkHref.contains("Flipkart")) {
                postDealOnFlipkartWrapper(amazonLinkHref);
            }

            // driver.get("http://google.com");
            driver.close();
            driver.switchTo().window(tabs.get(0));
        } catch (Exception e) {
            //e.printStackTrace();
            // previousFirstDealTitle = firstAmazonDealTitle;
            // logger.info("Can't Post " + amazonLink.getText());

            driver.switchTo().window(tabs.get(1));
            driver.close();
            driver.switchTo().window(tabs.get(0));
            // throw e;
        }
    }

    @BeforeTest
    public void beforeTest() {
        //System.setProperty("webdriver.chrome.driver", "C:\\Users\\rohan\\OneDrive\\Documents\\chromedriver.exe");
        //System.setProperty("webdriver.gecko.driver", "C:\\Users\\arora\\OneDrive\\Documents\\geckodriver.exe");
        //System.setProperty("webdriver.edge.driver", "C:\\Users\\arora\\Documents\\msedgedriver.exe");

        String filepath = ".\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", filepath);

        driver = new ChromeDriver();

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));


        //driver = new FirefoxDriver();
        //driver = new EdgeDriver();
        wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(20));
        js = (JavascriptExecutor) driver;
        driver.manage().window().maximize();
        try {
            // loginIntoAmazon("rohanmadaan.1997@gmail.com", "nonstopdeals@1322");
            // loginIntoAmazon("nonstopdeals.in@gmail.com", "NONSTOPDEALS@1322");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // driver.get(newPostUrl);
        logIntoBlog(newPostUrl, "scriptUser", "123456456@sS");

    }

    public void logIntoBlog(String newPostUrl, String username, String password) {
        driver.get(newPostUrl);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }
        driver.findElement(By.id("user_login")).sendKeys(username);
        driver.findElement(By.id("user_pass")).sendKeys(password);
        driver.findElement(By.id("wp-submit")).click();
    }

    public Map<String, String> getFieldsFromAmazon() throws Exception {
        Thread.sleep(1000);
        Map<String, String> mapToReturn = new HashMap<>();
        String oldPrice = null;
        String newPrice = null;
        String actualTitle = null;
        String specialDealType = null;
        String imageURL = null;
        String dealPercentage = null;
        StringBuilder features = new StringBuilder();
        String couponDiscount = null;
        boolean sendOnTelegram = true;
        logger.info("Fetching oldprice");
        WebElement oldPriceElement = driver.findElement(By.cssSelector("span[data-a-strike=\"true\"]"));
        if (oldPriceElement != null) {
            oldPrice = oldPriceElement.getText();
        } else {
            oldPriceElement = getElementIfExist("#priceblock_ourprice", css);
            if (oldPriceElement != null) {
                oldPrice = oldPriceElement.getText();
            }
        }
        logger.info("Oldprice fetched");
        logger.info("Fetching newprice");
        //WebElement newPriceElement = getElementIfExist("#priceblock_dealprice", css);
        WebElement newPriceElement = getElementIfExist("priceToPay", className);

        if (newPriceElement != null) {
            newPrice = newPriceElement.getText();
            if(newPrice.contains("\n")) {
                newPrice = newPrice.split("\n")[0];
            }
        } else if (getElementIfExist("apexPriceToPay", className) != null) {
            newPriceElement = getElementIfExist("apexPriceToPay", className);
            newPrice = newPriceElement.getText();
        }
        logger.info("Newprice fetched");
        WebElement applyCouponDom = getElementIfExist("//*[@id=\"vpcButton\"]/div/label/span", xpath);

        if (applyCouponDom != null) {
            couponDiscount = applyCouponDom.getText();
        }

        if (getElementIfExist("goldboxBuyBox", id) != null) {
            specialDealType = lightningDeal;

            long dealPercentageDomSize;
            dealPercentageDomSize = (Long) js.executeScript(
                    "return document.getElementsByClassName('a-size-small a-color-base a-text-bold').length");

            if (dealPercentageDomSize > 1 && couponDiscount != null) {
                dealPercentage = (String) js.executeScript(
                        "return document.getElementsByClassName('a-size-small a-color-base a-text-bold')[1].innerText;");
            } else if (dealPercentageDomSize > 0) {
                dealPercentage = (String) js.executeScript(
                        "return document.getElementsByClassName('a-size-small a-color-base a-text-bold')[0].innerText;");
            }
        }

        logger.info("Fetching title");
        WebElement titleElement = getElementIfExist("productTitle", id);
        if (titleElement != null) {
            actualTitle = titleElement.getText();
        }

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }
        logger.info("Title Fetched");
        logger.info("Fetching image URL");
        WebElement imageURLElement = getElementIfExist("//*[@id=\"landingImage\"]", xpath);

        if (imageURLElement != null) {
            imageURL = imageURLElement.getAttribute("src");
        }

        if (imageURLElement != null) {
            imageURL = imageURLElement.getAttribute("src");
        }
        logger.info("Image URL fetched");

        int count = 1;
        String affilateLink = fetchAffilateLink();
        while ((affilateLink == null || affilateLink.length() < 5) && count < 3) {
            driver.get(driver.getCurrentUrl());
            affilateLink = fetchAffilateLink();
            count++;
        }
        if (count == 3 && (affilateLink == null || affilateLink.length() < 5)) {
            // sendOnlyMessageOnTelegramForAmazon(ifsTitle, driver.getCurrentUrl(),
            // oldPrice, null);
            affilateLink = driver.getCurrentUrl();
            affilateLink = changeAmazonDealLink(affilateLink);
            sendOnTelegram = false;
            // throw new Exception();
        }

        WebElement featuresElement = getElementIfExist("featurebullets_feature_div", id);
        if (null != featuresElement) {
            features.append(featuresElement.getText());
            features.insert(0, "<ul><li>");
            features.append("</li></ul>");
            while (features.indexOf("\n") != -1) {
                int index = features.indexOf("\n");
                features.replace(index, index + 1, "</li><li>");
            }
        }

        if (null != oldPrice) {
            oldPrice = oldPrice.replaceAll(",", "");
            oldPrice = oldPrice.replaceAll("₹", "");
        }

        if (null != newPrice) {
            newPrice = newPrice.replaceAll(",", "");
            newPrice = newPrice.replaceAll("₹", "");
        }

        /**
         * Start Minus the discount coupon
         */
        if (!Objects.requireNonNull(newPrice).contains("-")) {
            if (couponDiscount != null) {
                String couponValue = couponDiscount.split(" ")[1];
                if (couponValue.charAt(couponValue.length() - 1) == '%') {
                    couponValue = couponValue.split("%")[0];
                    Double newPriceValue = Double.parseDouble(newPrice);
                    newPriceValue = newPriceValue - (newPriceValue * Double.parseDouble(couponValue) / 100);
                    newPrice = newPriceValue.toString();
                } else {
                    Double newPriceValue = Double.parseDouble(newPrice);
                    newPriceValue = newPriceValue - Double.parseDouble(couponValue);
                    newPrice = newPriceValue.toString();
                }
            }

        }
        /**
         * End Minus the discount coupon
         */
        mapToReturn.put("oldPrice", oldPrice);
        mapToReturn.put("newPrice", newPrice);
        mapToReturn.put("postTitle", actualTitle);
        mapToReturn.put("features", features.toString());
        mapToReturn.put("affilateLink", affilateLink);
        mapToReturn.put(specialDealKey, specialDealType);
        mapToReturn.put("imageURL", imageURL);
        mapToReturn.put("dealPercentage", dealPercentage);
        mapToReturn.put("couponDiscount", couponDiscount);
        mapToReturn.put("sendOnTelegram", Boolean.toString(sendOnTelegram));
        return mapToReturn;
    }

	/*private String changeAmazonDealLink(String unshortenUrl) throws URISyntaxException, UnsupportedEncodingException {

		Map<String, List<String>> queryParams = splitQuery(unshortenUrl);

		Map<String, List<String>> finalQueryParams = new HashMap<String, List<String>>();

		List<String> amazonAffialteIdList = new ArrayList<String>();
		amazonAffialteIdList.add("dealsalert13-21");

		if (queryParams.containsKey("tag")) {
			finalQueryParams.put("tag", amazonAffialteIdList);
		} else {
			finalQueryParams.put("tag", amazonAffialteIdList);
		}

		if (queryParams.containsKey("keyword")) {
			queryParams.remove("keyword");
		}

		if (queryParams.containsKey("keywords") && queryParams.get("keywords") != null
				&& queryParams.get("keywords").size() > 0
				&& (queryParams.get("keywords").contains("copied") || queryParams.get("keywords").contains("copy")
						|| queryParams.get("keywords").get(0).contains("telegram")
						|| queryParams.get("keywords").get(0).toLowerCase().contains("telegram"))) {
			queryParams.remove("keywords");
		}

		String originalURL = unshortenUrl.split("\\?")[0];

		Iterator<String> it = finalQueryParams.keySet().iterator();
		URIBuilder b = new URIBuilder(originalURL);

		while (it.hasNext()) {
			String key = it.next();
			if (key.contains("amazon.in")) {
				continue;
			}
			List<String> value = finalQueryParams.get(key);
			if (!value.isEmpty()) {
				// b.addParameter(key, value);
				value.stream().forEach(s -> b.addParameter(key, s));
			}
		}

		if (b.toString().contains("tag=" + amazonAffialteIdList.get(0)) && !b.toString().contains("afflepay1007-21")) {
			return URLDecoder.decode(b.toString());
		} else {
			return null;
		}
	}*/

    public static Map<String, List<String>> splitQuery(String url) throws UnsupportedEncodingException {
        url = URLDecoder.decode(url);
        url = url.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
        url = url.replaceAll("\\+", "%2B");
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        String[] pairs;
        if (url.contains("?")) {
            pairs = url.split("\\?")[1].split("&");
        } else {
            pairs = url.split("&");
        }
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<>());
            }
            final String value = idx > 0 && pair.length() > idx + 1
                    ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
                    : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }

    public String removeQueryParameters(String url) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        uriBuilder.removeQuery();

        return uriBuilder.build().toString();
    }

    private String removeAnyIFSOccurance(String input) {
        String regexStr = "i+n+d+i+a+f+r+e+e+s+t+u+f+f+";

        Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("nonstopdeals");
    }

    public String fetchAffilateLink() throws Exception {

        return changeAmazonDealLink(driver.getCurrentUrl());
    }

    public boolean postDealOnWebsite(Map<String, String> returnMap) throws Exception {
        Double oldPriceValue = (double) 0;
        Double newPriceValue = (double) 0;
        String newPrice = null;
        if (returnMap.get("oldPrice") != null) {
            String oldPrice = returnMap.get("oldPrice");
            if (oldPrice.contains("₹")) {
            }
            oldPriceValue = Double.parseDouble(returnMap.get("oldPrice"));
        }

        if (returnMap.get("newPrice").contains("-")) {
            newPrice = returnMap.get("newPrice");
        } else {
            String newPriceForParsing = returnMap.get("newPrice");
            newPriceForParsing = newPriceForParsing.replaceAll(",", "");
            newPriceValue = Double.parseDouble(newPriceForParsing);
        }

        String actualTitle = returnMap.get("postTitle");
        String features = returnMap.get("features");
        features = features.replaceAll("'", "");
        actualTitle = actualTitle.replaceAll("'", "");
        String affilateLink = returnMap.get("affilateLink");
        String newTitle;
        if (newPrice == null) {
            newTitle = /* "Buy " + */ actualTitle + " at Rs. " + newPriceValue.intValue();
        } else {
            newTitle = /* "Buy " + */actualTitle + " at Rs. " + newPrice;
        }

        driver.get(newPostUrl);

        WebElement postTitle = getElementIfExist("post_title", name);
        if (null != postTitle) {
            try {
                js.executeScript("document.getElementsByName('post_title')[0].value = '" + newTitle + "'");
            } catch (Exception e) {
                postTitle.sendKeys(newTitle);
            }

        }

        WebElement contentHTML = getElementIfExist("content-html", id);
        if (null != contentHTML) {
            clickElement(contentHTML);
        }

        /*
         * WebElement bodyElement = getElementIfExist("wp-editor-area", className); if
         * (null != bodyElement) { bodyElement.sendKeys(features); }
         */

        try {
            js.executeScript("document.getElementsByClassName('wp-editor-area')[0].value = '" + features + "'");
        } catch (Exception e) {
            WebElement bodyElement = getElementIfExist("wp-editor-area", className);
            if (null != bodyElement) {
                bodyElement.sendKeys(features);
            }
        }

        // Adding Content Egg
        // addContentEggInPost(actualTitle);
        addPostAttributes(returnMap);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement publishButton = getElementIfExist("publish", id);
        clickElement(publishButton);

        return true;
    }

    public void addPostAttributes(Map<String, String> returnMap) {
        Double oldPriceValue = (double) 0;
        Double newPriceValue = (double) 0;
        String newPrice = null;
        if (returnMap.get("oldPrice") != null) {
            oldPriceValue = Double.parseDouble(returnMap.get("oldPrice"));
        }

        if (returnMap.get("newPrice").contains("-")) {
            newPrice = returnMap.get("newPrice");
        } else {
            String newPriceForParsing = returnMap.get("newPrice");
            newPriceForParsing = newPriceForParsing.replaceAll(",", "");
            newPriceValue = Double.parseDouble(newPriceForParsing);
        }
        String affilateLink = returnMap.get("affilateLink");

        getElementIfExist("//*[@id=\"post_rehub_offers\"]/div[1]/div/button[3]", xpath).click();

        WebElement productURL = getElementIfExist("rehub_offer_product_url", id);
        if (null != productURL) {
            productURL.sendKeys(affilateLink);
        }

        WebElement offerOldPriceTextField = getElementIfExist("//*[@id=\"rehub_offer_product_price_old\"]", xpath);
        WebElement offerSalePriceTextField = getElementIfExist("//*[@id=\"rehub_offer_product_price\"]", xpath);
        if (null != offerOldPriceTextField && null != offerSalePriceTextField) {
            if (newPrice == null) {
                offerSalePriceTextField.sendKeys("&#8377; " + newPriceValue.intValue());
                if (oldPriceValue.intValue() == newPriceValue.intValue()) {
                    oldPriceValue = (double) 0;
                }
            } else {
                offerSalePriceTextField.sendKeys("&#8377; " + newPrice);
            }
            if (oldPriceValue != 0) {
                offerOldPriceTextField.sendKeys("&#8377; " + oldPriceValue.intValue());
            }

        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement imageURLElement = getElementIfExist("knawatfibu_url", id);
        if (null != imageURLElement && null != returnMap.get("imageURL")) {
            js.executeScript("document.getElementById('knawatfibu_url').value = '" + returnMap.get("imageURL") + "'");
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void postOnTelegram(Map<String, String> returnMap) throws Exception {

        try {
            // Select Yes on Auto Publish
            js.executeScript("document.getElementsByName('afap_auto_post')[0].value='yes'");
        } catch (Exception e) {
            // TODO: handle exception
        }

        Double oldPriceValue = (double) 0;
        Double newPriceValue = (double) 0;
        String newPrice = null;
        if (returnMap.get("oldPrice") != null) {
            oldPriceValue = Double.parseDouble(returnMap.get("oldPrice"));
        }

        if (returnMap.get("newPrice").contains("-")) {
            newPrice = returnMap.get("newPrice");
        } else {
            String newPriceForParsing = returnMap.get("newPrice");
            newPriceForParsing = newPriceForParsing.replaceAll(",", "");
            newPriceValue = Double.parseDouble(newPriceForParsing);
        }
        String actualTitle = returnMap.get("postTitle");
        String newTitle;
        actualTitle = actualTitle.replaceAll("'", "");
        if (newPrice == null) {
            newTitle = /* "Buy " + */"*" + actualTitle + " at Rs. " + newPriceValue.intValue() + " @ Amazon";
        } else {
            newTitle = /* "Buy " + */"*" + actualTitle + " at Rs. " + newPrice + " @ Amazon";
        }

        if (returnMap.get(specialDealKey) != null && returnMap.get(specialDealKey).equalsIgnoreCase(lightningDeal)) {
            if (returnMap.get("dealPercentage") != null) {
                newTitle = "*(Lightning Deal! " + returnMap.get("dealPercentage") + " Claimed Grab Fast!!)* \n\n"
                        + newTitle;
            } else {
                newTitle = "*(Lightning Deal!) Grab Fast!!* \n\n" + newTitle;
            }
        }

        if (oldPriceValue.intValue() != 0 && newPriceValue != 0
                && oldPriceValue.intValue() != newPriceValue.intValue()) {
            newTitle = newTitle + " (Original Price Rs. " + oldPriceValue.intValue() + ")*";
        } else {
            newTitle = newTitle + "*";
        }

        if (null != returnMap.get("couponDiscount")) {
            newTitle = newTitle + "\n\n " + returnMap.get("couponDiscount");
        }

        WebElement postOnTelCheckBox = getElementIfExist("telegram_m_send", id);
        clickElement(postOnTelCheckBox);

        WebElement shortLinkElement = getElementIfExist("shortlink", id);
        String shortLink = null;
        if (shortLinkElement != null) {
            shortLink = shortLinkElement.getAttribute("value");
        }
        WebElement telMessageBody = getElementIfExist("telegram_m_send_content", id);
        telMessageBody.clear();
        telMessageBody.sendKeys(newTitle);
        telMessageBody.sendKeys("\n \n");
        telMessageBody.sendKeys(shortLink);
        telMessageBody.sendKeys("\n");
        WebElement publishButton = getElementIfExist("publish", id);
        clickElement(publishButton);
    }

    public WebElement getElementIfExist(String property, String propertyType) throws NoSuchElementException {
        // logger.info("Getting WebElement: " + property);
        if (propertyType.equalsIgnoreCase("id")) {
            if (driver.findElements(By.id(property)).size() > 0) {
                return driver.findElement(By.id(property));
            }
        } else if (propertyType.equalsIgnoreCase("css")) {
            if (driver.findElements(By.cssSelector(property)).size() > 0) {
                return driver.findElement(By.cssSelector(property));
            }
        } else if (propertyType.equalsIgnoreCase("xpath")) {
            if (driver.findElements(By.xpath(property)).size() > 0) {
                return driver.findElement(By.xpath(property));
            }
        } else if (propertyType.equalsIgnoreCase("name")) {
            if (driver.findElements(By.name(property)).size() > 0) {
                return driver.findElement(By.name(property));
            }
        } else if (propertyType.equalsIgnoreCase("className")) {
            if (driver.findElements(By.className(property)).size() > 0) {
                return driver.findElements(By.className(property)).get(0);
            }
        }
        return null;
    }

    public void clickElement(WebElement element) throws Exception {
        // logger.info("Clicking element: " + element.getText());
        try {
            if (element.isDisplayed()) {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
            }
        } catch (Exception e) {
            js.executeScript("document.getElementById('" + element.getAttribute("id") + "').click()");
            // throw e;
        }
    }

    public void postDealWrapper(String amazonURL) throws Exception {
        logger.info("Opening amazon URL");
        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
        driver.navigate().to(amazonURL.split("url=")[1]);
        //driver.get(amazonURL.split("url=")[1]);
        logger.info("Getting parameters from amazon");
        Map<String, String> returnMap = getFieldsFromAmazon();
        logger.info("Opening Our Website");
        postDealOnWebsite(returnMap);

        // postOnTelegram(returnMap);
    }

    public void postIFSDeal(String ifsAmazonLink, String ifsPrice, String dealType) throws Exception {
        js.executeScript("window.open('', '_blank');");

        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        String amazonLinkHref = null;
        postAmazonIFSDeal(ifsAmazonLink, ifsPrice, tabs, null);
    }

    private void postAmazonIFSDeal(String ifsAmazonLink, String ifsPrice, ArrayList<String> tabs,
                                   String amazonLinkHref) {
        try {
            driver.switchTo().window(tabs.get(1));
            amazonLinkHref = ifsAmazonLink;
            postDealWrapper(amazonLinkHref);

            driver.close();
            driver.switchTo().window(tabs.get(0));
        } catch (Exception e) {

            driver.close();
            driver.switchTo().window(tabs.get(0));
            /*
             * try { String linkText = postTitle; String amazonLink = amazonLinkHref;
             * driver.get(amazonLink); if
             * (driver.findElements(By.cssSelector("#availability > span")).size() > 0 &&
             * driver.findElements(By.cssSelector("#availability > span")).get(0).getText()
             * .contains("Currently unavailable")) { throw new Exception(); } int count = 1;
             * String affilatedLink = fetchAffilateLink(); while ((affilatedLink == null ||
             * affilatedLink.length() < 5) && count < 3) { driver.get(amazonLink);
             * affilatedLink = fetchAffilateLink(); count++; } if (count == 3 &&
             * (affilatedLink == null || affilatedLink.length() < 5)) {
             * sendOnlyMessageOnTelegramForAmazon(linkText, driver.getCurrentUrl(), null,
             * null); throw e; } sendOnlyMessageOnTelegramForAmazon(linkText, affilatedLink,
             * null, null); } catch (Exception e2) { logger.info("Can't post - " +
             * postTitle); } driver.close(); driver.switchTo().window(tabs.get(0)); // throw
             * e;
             */}
    }

    public void postDealOnFlipkartWrapper(String flipkartURL) throws Exception {
        logger.info("Opening flipkart URL");
        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(40));
        driver.get(flipkartURL);
        driver.switchTo().window(tabs.get(1));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Getting parameters from amazon");
        Map<String, String> returnMap = getAttributesFromFlipkart();
        boolean dealPosted = postDealOnWebsite(returnMap);
        // postOnTelegram(returnMap);
        /*
         * if (postSingleDeal) { sendOnlyMessageOnTelegramForAmazon(ifsTitle, "" !=
         * returnMap.get("affilateLink") ? returnMap.get("affilateLink") :
         * driver.getCurrentUrl(), returnMap.get("oldPrice"),
         * returnMap.get("couponDiscount")); }
         */
    }

    public void postIFSFlipkartDeal(String ifsFlipkartLink) throws Exception {

        // js.executeScript("window.open('', '_blank');");

        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        String postTitle = null;
        String flipkartLinkHref;
        try {
            driver.switchTo().window(tabs.get(1));
            flipkartLinkHref = ifsFlipkartLink;
            postDealOnFlipkartWrapper(flipkartLinkHref);

            driver.close();
            driver.switchTo().window(tabs.get(0));
        } catch (Exception e) {
            try {
                String linkText = null;
                if (!Objects.requireNonNull(driver).getCurrentUrl().contains("flipkart")) {
                    driver.get(ifsFlipkartLink);
                }
                try {
                    String currentURL = driver.getCurrentUrl();
                    String affilateURL = changeIFSAffilteToOurAffilateForFlipkart(currentURL);
                    sendOnlyMessageOnTelegramForFlipkart(null, affilateURL, null, null);
                } catch (Exception e2) {
                    throw new Exception();
                }
                e.printStackTrace();
            } catch (Exception e2) {
                logger.info("Can't post Flipkart Post - " + null);
            }
            driver.close();
            driver.switchTo().window(tabs.get(0));
            // throw e;
        }

    }

    private Map<String, String> getAttributesFromFlipkart() throws Exception {
        ArrayList<String> tabs = new ArrayList(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        Map<String, String> mapToReturn = new HashMap<>();
        String oldPrice;
        String newPrice;
        String actualTitle;
        String imageURL;
        String affilateLink;
        String currentURL;
        StringBuilder features = new StringBuilder();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
        }

        oldPrice = (String) js.executeScript("return document.getElementsByClassName('_3I9_wc _2p6lqe')[0].innerText");
        newPrice = (String) js.executeScript("return document.getElementsByClassName('_30jeq3 _16Jk6d')[0].innerText");
        actualTitle = (String) js.executeScript("return document.getElementsByClassName('B_NuCI')[0].innerText");
        imageURL = (String) js.executeScript(
                "return document.getElementsByClassName('_396cs4 _2amPTt _3qGmMb  _3exPp9')[0].getAttribute('src')");

        // affilateLink = fetchFlipkartAffilateLink();
        try {
            features.append((String) js.executeScript("return document.getElementsByClassName('_2418kt')[0].innerText"));
            features.insert(0, "<ul><li>");
            features.append("</li></ul>");
            while (features.indexOf("\n") != -1) {
                int index = features.indexOf("\n");
                features.replace(index, index + 1, "</li><li>");
            }
        } catch (Exception e) {
            // Do Nothing
        }

        oldPrice = oldPrice.replaceAll(",", "");
        newPrice = newPrice.replaceAll(",", "");
        oldPrice = oldPrice.substring(1);
        newPrice = newPrice.substring(1);
        currentURL = driver.getCurrentUrl();

        affilateLink = changeIFSAffilteToOurAffilateForFlipkart(currentURL);

        mapToReturn.put("oldPrice", oldPrice);
        mapToReturn.put("newPrice", newPrice);
        mapToReturn.put("postTitle", actualTitle);
        mapToReturn.put("features", features.toString());
        mapToReturn.put("imageURL", imageURL);
        mapToReturn.put("affilateLink", affilateLink);
        return mapToReturn;
    }

    public void sendOnlyMessageOnTelegramForAmazon(String linkText, String affilatedLink, String originalPrice,
                                                   String dealPercentage) throws URISyntaxException, UnsupportedEncodingException {
        if (sendToTelegram) {
            WebElement textArea = doCommonProcessingForSendingTelegramMessage(linkText, originalPrice, dealPercentage);
            affilatedLink = changeAmazonDealLink(affilatedLink);
            textArea.sendKeys(affilatedLink);
            WebElement sendNowButton = getElementIfExist("submit", id);
            sendNowButton.click();
        }
    }

    public void sendOnlyMessageOnTelegramForFlipkart(String linkText, String affilatedLink, String originalPrice,
                                                     String dealPercentage) {
        if (sendToTelegram) {
            WebElement textArea = doCommonProcessingForSendingTelegramMessage(linkText, originalPrice, dealPercentage);
            affilatedLink = changeIFSAffilteToOurAffilateForFlipkart(affilatedLink);
            textArea.sendKeys(affilatedLink);
            WebElement sendNowButton = getElementIfExist("submit", id);
            sendNowButton.click();
        }
    }

    private String changeIFSAffilteToOurAffilateForFlipkart(String affilateLink) {
        boolean shortURL = false;
        while (affilateLink.contains("hello19th")) {
            affilateLink = affilateLink.replace("hello19th", "vishalkum20");
            shortURL = true;
        }
        while (affilateLink.contains("indiafreestuff")) {
            affilateLink = affilateLink.replace("indiafreestuff", "nonstopdeals");
            shortURL = true;
        }
        while (affilateLink.contains("ifs")) {
            affilateLink = affilateLink.replace("ifs", "nsd");
        }

        affilateLink = removeAnyIFSOccurance(affilateLink);

        if (affilateLink.length() > 50) {
            shortURL = true;
        }

        try {
            if (shortURL) {
                affilateLink = shortURL(affilateLink);
            }
        } catch (Exception e) {
            // Do Nothing
        }
        return affilateLink;
    }

    public static String shortURL(String longUrl) {

        String access_token = "d3701debdb985099802e5a20d1bd77aa04b94779";
        // Bitly bitly = Bit.ly(access_token);
        // String shortUrl = bitly.shorten(longUrl);
        return longUrl;
    }

    private WebElement doCommonProcessingForSendingTelegramMessage(String linkText, String originalPrice,
                                                                   String dealPercentage) {
        driver.get(telegramSendURL);
        WebElement textArea = getElementIfExist("telegram_new_message", name);
        // textArea.sendKeys("**" + linkText + "**\n\n");
        linkText = linkText.replaceAll("'", "");
        textArea.sendKeys(linkText + "\n\n");

        if (null != originalPrice) {
            textArea.sendKeys("MRP Rs. " + originalPrice.trim() + "\n\n");
        }

        if (null != dealPercentage) {
            textArea.sendKeys(dealPercentage + "\n\n");
        }
        return textArea;
    }

    private String changeAmazonDealLink(String unshortenUrl)
            throws URISyntaxException, UnsupportedEncodingException {
        unshortenUrl = URLDecoder.decode(unshortenUrl);

//        try {
//            int asinIndex = unshortenUrl.indexOf("/dp/");
//            if(asinIndex != -1) {
//                String asin = unshortenUrl.substring(asinIndex+4, asinIndex + 14);
//                String ourURL = "https://www.amazon.in/dp/" + asin + "/?tag=dealsalert13-21";
//                return ourURL;
//            }
//        } catch (Exception e) {
//            // Do nothing
//        }

        Map<String, List<String>> queryParams = splitQuery(unshortenUrl);

        if (queryParams.containsKey("tag")) {
            queryParams.put("tag", amazonAffialteIdList);
        } else {
            queryParams.put("tag", amazonAffialteIdList);
        }

        queryParams.remove("keyword");

        if (queryParams.containsKey("keywords") && queryParams.get("keywords") != null
                && queryParams.get("keywords").size() > 0
                && (queryParams.get("keywords").contains("copied") || queryParams.get("keywords").contains("copy")
                || queryParams.get("keywords").get(0).contains("telegram")
                || queryParams.get("keywords").get(0).toLowerCase().contains("telegram"))) {
            queryParams.remove("keywords");
        }

        String originalURL = unshortenUrl.split("\\?")[0];

        Iterator<String> it = queryParams.keySet().iterator();
        URIBuilder b = new URIBuilder(originalURL);

        while (it.hasNext()) {
            String key = it.next();
            if (key.contains("amazon.in")) {
                continue;
            }
            List<String> value = queryParams.get(key);
            if (!value.isEmpty()) {
                // b.addParameter(key, value);
                value.forEach(s -> b.addParameter(key, s));
            }
        }

        if (b.toString().contains("tag=" + amazonAffialteIdList.get(0)) && !b.toString().contains("afflepay1007-21")) {
            return b.toString();
        } else {
            return null;
        }
    }

    @AfterTest
    public void afterTest() {
        logger.info(postsList.get(postsList.size() - 1));
        //driver.close();
    }
}
