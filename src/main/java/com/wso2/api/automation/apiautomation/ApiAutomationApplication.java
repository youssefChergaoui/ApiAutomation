package com.wso2.api.automation.apiautomation;

import com.wso2.api.automation.apiautomation.models.Revision;
import com.wso2.api.automation.apiautomation.services.HttpGenericServices;
import com.wso2.api.automation.apiautomation.services.OAuth2TokenServices;
import com.wso2.api.automation.apiautomation.services.PublisherServices;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ApiAutomationApplication {

    public static void main(String[] args) {
        //SpringApplication.run(ApiAutomationApplication.class, args);
        String accessToken = null;
        List<Revision> revisions = null;
        String apiId = null;
        try {

            //Get Swagger Content
            String swaggetContent = null;
            try{
                swaggetContent = HttpGenericServices.getSwaggerContentFromUrll("https://petstore.swagger.io/v2/swagger.json");
                //File swaggerContent = new File("C:\\Users\\CHERGAOUIY\\Desktop\\sg.json");
                System.out.println(swaggetContent);
            }catch (Exception e) {
                System.err.println("Error in function getSwaggerContentFromUrll : " + e.getMessage());
            }
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

            //Generate access Token
            try{
                accessToken = OAuth2TokenServices.oauth2PasswordCredentials("admin","admin");
            }catch (Exception e) {
                System.err.println("Error in function oauth2PasswordCredentials : " + e.getMessage());
            }
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

            //Import Swagger File in wso2 publisher
            try{
                apiId = PublisherServices.importSwaggerContent(swaggetContent,accessToken);
            }catch (Exception e) {
                System.err.println("Error in function importSwaggerContent : " + e.getMessage());
            }
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

            //Test if apiId is null
            if(apiId == null){
                System.out.println("the apiId was not returned");
            }else System.out.println("ApiId : "+apiId);


            //Print revisions
            try{
                revisions = PublisherServices.getRevisionsOfApi(apiId,accessToken);
                System.out.println("revisions : "+revisions);
            }catch (Exception e) {
                System.err.println("Error in function getRevisionsOfApi : " + e.getMessage());
            }
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

            //Test if the list of revisions is >= to 5 (max of revision can be created 5) ==> remove a revision
            try{
                if(revisions.size() >= 5){
                    PublisherServices.deleteRevision(apiId,revisions.get(0).getId(),accessToken);
                }else System.out.println("Nbr of revision : "+revisions.size()+" no revision deleted");
            }catch (Exception e) {
                System.err.println("Error in function deleteRevision : " + e.getMessage());
            }
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

            //Create API Revision
            String revisionId = null;
            try{
                revisionId = PublisherServices.createAPIRevision(apiId,accessToken);
                System.out.println("Revision Id : "+revisionId);
            }catch (Exception e) {
                System.err.println("Error in function createAPIRevision : " + e.getMessage());
            }
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

            //Deploy API Revision
            try{
                PublisherServices.deployAPIRevision(apiId,revisionId, accessToken);
            }catch (Exception e) {
                System.err.println("Error in function deployAPIRevision : " + e.getMessage());
            }
            System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||||||||");

            //Change API status to publich
            try{
                PublisherServices.publishAPI(apiId,accessToken);
            }catch (Exception e) {
                System.err.println("Error in function publishAPI : " + e.getMessage());
            }


            //Update API
            //UpdateApiRequest updateApiRequest=new UpdateApiRequest("","","");
            //PublisherServices.updateApi(apiId,accessToken,updateApiRequest);

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
    }
    }

}
