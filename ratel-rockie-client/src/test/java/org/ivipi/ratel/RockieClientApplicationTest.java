package org.ivipi.ratel;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lianjiatech.retrofit.spring.boot.annotation.RetrofitScan;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.rockie.client.api.RockieApi;
import org.ivipi.ratel.rockie.common.model.Content;
import org.ivipi.ratel.rockie.common.model.ContentAdd;
import org.ivipi.ratel.rockie.common.model.DocumentAdd;
import org.ivipi.ratel.rockie.common.model.Folder;
import org.ivipi.ratel.rockie.common.model.FolderAdd;
import org.ivipi.ratel.rockie.common.model.FolderDelete;
import org.ivipi.ratel.rockie.common.model.FolderPage;
import org.ivipi.ratel.rockie.common.model.FolderUpdate;
import org.ivipi.ratel.system.client.api.SystemApi;
import org.ivipi.ratel.system.client.api.TokenSignService;
import org.ivipi.ratel.system.common.model.CustomerAdd;
import org.ivipi.ratel.system.common.model.CustomerInfo;
import org.ivipi.ratel.system.common.model.CustomerPassword;
import org.ivipi.ratel.system.common.model.CustomerUpdate;
import org.ivipi.ratel.system.common.model.License;
import org.ivipi.ratel.system.common.model.LicenseAdd;
import org.ivipi.ratel.system.common.model.LicensePage;
import org.ivipi.ratel.system.common.model.Login;
import org.ivipi.ratel.system.common.model.Order;
import org.ivipi.ratel.system.common.model.Product;
import org.ivipi.ratel.system.common.model.ProductAdd;
import org.ivipi.ratel.system.common.model.ProductDelete;
import org.ivipi.ratel.system.common.model.ProductPage;
import org.ivipi.ratel.system.common.model.ProductUpdate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RetrofitScan("org.ivipi.ratel")
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RockieClientApplicationTest {

    @Autowired
    private SystemApi systemApi;

    @Autowired
    private RockieApi rockieApi;

    @Autowired
    private TokenSignService tokenSignService;

    private CustomerInfo testCustomerInfo;
    private Product testProduct;
    private License testLicense;
    private Folder testFolder;

    @BeforeAll
    public void beforeAll(){
        log.info("Start test now ...");
        String name = generateName();
        String password = generatePassword();

        register(name, password);
        login(name, password);
        addTestProduct();
        addTestLicense();
        addTestFolder();
    }

    @BeforeEach
    public  void beforeEach(){

    }

    @AfterAll
    public void afterAll(){
        log.info("End test now ...");
        Result result = systemApi.logout();
        assertNotNull(result);
        assertTrue(result.isSuccess());

    }

    @AfterEach
    public void afterEach(){

    }

    private void login(String name, String password) {
        Login login = new Login();
        login.setName(name);
        login.setPassword(password);
        Result result = systemApi.login(login);
        assertNotNull(result);
        assertTrue(result.isSuccess());
        String token = result.getData().toString();
        tokenSignService.setToken(token);
        Result<CustomerInfo> customerInfoResult = systemApi.info();
        assertNotNull(result);
        assertTrue(result.isSuccess());
        testCustomerInfo = customerInfoResult.getData();
    }

    private String getTimeString() {
        String timeString;
        timeString = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        return timeString;
    }

    private String generateName() {
        String namePrefix = "Customer";
        String nameSuffix = DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss");
        String name = namePrefix + nameSuffix;
        return name;
    }

    private void register(String name, String password) {
        CustomerAdd customerAdd = new CustomerAdd();
        customerAdd.setCustomerName(name);
        customerAdd.setPassword(password);
        Result result = systemApi.register(customerAdd);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    private String generatePassword() {
        return "Password1";
    }

    private void addTestProduct() {
        String productName = "Product" + getTimeString();
        addProduct(productName, true);
        testProduct = checkProduct(productName);
    }

    private void addTestLicense() {
        addLicense(testProduct.getProductId(), true);
        testLicense = checkLicense(testProduct.getProductId());
    }

    private void addTestFolder() {
        String folderName = "Folder" + getTimeString();
        addFolder(folderName, true);
        testFolder = checkFolder(folderName);
    }

    private void addProduct(String productName, boolean expectedResult) {
        ProductAdd productAdd = new ProductAdd();
        productAdd.setProductName(productName);
        Result result = systemApi.addProduct(productAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Product checkProduct(String productName) {
        ProductPage productPage = new ProductPage();
        productPage.setPageSize(99999);
        Result<Page<Product>> productResult = systemApi.getProducts(productPage);
        assertNotNull(productResult);
        assertTrue(productResult.isSuccess());
        long size = productResult.getData().getRecords().size();
        boolean checkProduct = false;
        Product product = null;
        for (long i = size - 1; i >= 0; i--) {
            Product theProduct = productResult.getData().getRecords().get((int) i);
            if (theProduct.getProductName().equals(productName)) {
                checkProduct = true;
                product = theProduct;
            }
        }
        assertTrue(checkProduct);
        return product;
    }

    private void addLicense(Long productId, boolean expectedResult) {
        LicenseAdd licenseAdd = new LicenseAdd();
        licenseAdd.setProductId(productId);
        licenseAdd.setRemark("Add License Test");
        Result result = systemApi.addLicense(licenseAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private License checkLicense(Long productId) {
        LicensePage licensePage = new LicensePage();
        licensePage.setPageSize(99999);
        Result<Page<License>> licenseResult = systemApi.getLicenses(licensePage);
        long size = licenseResult.getData().getRecords().size();
        boolean checkLicense = false;
        License license = null;
        for (long i = size - 1; i >= 0; i--) {
            License theLicense = licenseResult.getData().getRecords().get((int) i);
            if (theLicense.getProductId().equals(productId) && theLicense.getCustomerId().equals(testCustomerInfo.getCustomerId())) {
                checkLicense = true;
                license = theLicense;
                break;
            }
        }
        assertTrue(checkLicense);
        return license;
    }


    private void addFolder(String folderName, boolean expectedResult) {
        FolderAdd folderAdd = new FolderAdd();
        folderAdd.setFolderName(folderName);
        Result result = rockieApi.addFolder(folderAdd);
        assertNotNull(result);
        if (expectedResult) {
            assertTrue(result.isSuccess());
        } else {
            assertFalse(result.isSuccess());
        }
    }

    private Folder checkFolder(String folderName) {
        FolderPage folderPage = new FolderPage();
        folderPage.setPageSize(99999);
        Result<Page<Folder>> folderResult = rockieApi.getFolders(folderPage);
        assertNotNull(folderResult);
        assertTrue(folderResult.isSuccess());
        int folderCount = folderResult.getData().getRecords().size();
        Folder folder = null;
        for(int i = 0; i< folderCount; i ++) {
            Folder theFolder = folderResult.getData().getRecords().get(i);
            if(folderName.equals(theFolder.getFolderName())) {
                folder = theFolder;
                break;
            }
        }
        assertNotNull(folder);
        return folder;
    }

    @Test
    public void testAddFolder() {
       log.info("Add Folder with new folder name");
       String testFolderName = "Test Add Folder" + getTimeString();
       addFolder(testFolderName, true);
       Folder folder = checkFolder(testFolderName);
       assertNotNull(folder);
       log.info("Add folder with existing folder name");
       addFolder(testFolderName, false);
    }

    @Test
    public void testDeleteFolder() {
        log.info("Test delete folder");
        String testFolderName = "Test Delete Folder" + getTimeString();
        addFolder(testFolderName, true);
        Folder folder = checkFolder(testFolderName);
        assertNotNull(folder);
        FolderDelete folderDelete = new FolderDelete();
        folderDelete.setFolderId(folder.getFolderId());
        Result deleteResult = rockieApi.deleteFolder(folderDelete);
        assertNotNull(deleteResult);
        assertTrue(deleteResult.isSuccess());
    }

    @Test
    public void testUpdateFolder() {
        log.info("Test update folder");
        String testFolderName = "Test Update Folder" + getTimeString();
        addFolder(testFolderName, true);
        Folder folder = checkFolder(testFolderName);
        assertNotNull(folder);
        FolderUpdate folderUpdate = new FolderUpdate();
        folderUpdate.setFolderId(folder.getFolderId());
        folderUpdate.setFolderName(testFolderName + " is updated");
        Result updateResult = rockieApi.updateFolder(folderUpdate);
        assertNotNull(updateResult);
        assertTrue(updateResult.isSuccess());
    }

    @Test
    public void testGetFolders() {
        log.info("Test get folder");
        String testFolderName = "Test Get Folder" + getTimeString();
        addFolder(testFolderName, true);
        Folder folder = checkFolder(testFolderName);
        assertNotNull(folder);
    }


    @Test
    public void testAddDocument() {
        ContentAdd contentAdd = new ContentAdd();
        contentAdd.setContentName("Test Content");
        contentAdd.setContent("Test Content");
        DocumentAdd documentAdd = new DocumentAdd();
        documentAdd.setDocumentName("Test Add Document");
        documentAdd.setContent(contentAdd);
        documentAdd.setFolderId(testFolder.getFolderId());
        Result result = rockieApi.addDocument(documentAdd);
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }
}
