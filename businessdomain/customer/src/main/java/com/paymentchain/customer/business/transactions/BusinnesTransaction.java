/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paymentchain.customer.business.transactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentchain.customer.entities.Customer;
import com.paymentchain.customer.entities.CustomerProduct;
import com.paymentchain.customer.exception.BusinessRuleException;
import com.paymentchain.customer.repository.CustomerRepository;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.netty.http.client.HttpClient;

/**
 *
 * @author rvega
 */
/**
 * Service class responsible for handling business transactions. This class acts
 * as a bridge between the repository and business logic layers, encapsulating
 * the operations related to customers and their associated data.
 */
@Service
public class BusinnesTransaction {

    /**
     * Injects a {@link WebClient.Builder} instance to configure and create
     * WebClient objects. This is typically used for making non-blocking HTTP
     * requests to external services.
     */
    @Autowired
    private WebClient.Builder webClientBuilder;

    /**
     * Automatically injected repository for managing Customer entities.
     */
    @Autowired
    CustomerRepository customerRepository;

    // Create a customized HttpClient instance.
    HttpClient client = HttpClient.create()
            // Set a connection timeout (in milliseconds).
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500)
            // Enable the SO_KEEPALIVE option to keep the connection alive.
            .option(ChannelOption.SO_KEEPALIVE, true)
            // Specify the idle time (in seconds) before sending a keep-alive probe (Epoll only).
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            // Specify the interval (in seconds) between keep-alive probes (Epoll only).
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            // Set the response timeout duration.
            .responseTimeout(Duration.ofSeconds(1))
            // Add handlers for read and write timeouts when a connection is established.
            .doOnConnected(connection -> {
                // Add a handler to manage read timeout (in milliseconds).
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));

                // Add a handler to manage write timeout (in milliseconds).
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });

    /**
     * Handles the logic for creating and saving a new customer. This method
     * also validates the products associated with the customer.
     */
    public Customer post(Customer input) throws BusinessRuleException, UnknownHostException, InterruptedException {
        // Check if the customer has associated products
        if (input.getProducts() != null) {
            // Iterate through the products to validate and associate them with the customer
            for (Iterator<CustomerProduct> it = input.getProducts().iterator(); it.hasNext();) {
                CustomerProduct dto = it.next();

                // Fetch the product name using the product ID
                String productName = getProductName(dto.getProductId());
                // If the product name is blank (product does not exist), throw a validation exception
                if (productName.isBlank()) {
                    BusinessRuleException businessRuleException = new BusinessRuleException(
                            "1025", // Custom error code for missing product
                            "Validation error, product ID  " + dto.getProductId() + " does not exists", // Error message
                            HttpStatus.PRECONDITION_FAILED // HTTP status indicating the failure
                    );
                    throw businessRuleException;
                } else {
                    // If the product exists, associate it with the customer
                    dto.setCustomer(input);
                }
            }
        }

        // Save the customer (along with validated products) to the repository
        Customer save = customerRepository.save(input);

        // Return the saved customer
        return save;
    }

    /**
     * Retrieves a customer by their unique code and enriches their products
     * with additional details.
     *
     * @param code the unique code identifying the customer
     * @return the customer with enriched product details
     */
    public Customer get(String code) {
        // Retrieve the customer from the repository based on the provided code.
        Customer customer = customerRepository.findByCode(code);
        if (customer != null) {
            // Get the list of products associated with the customer.
            List<CustomerProduct> products = customer.getProducts();

            // Enrich each product with its name by calling the external product service.
            products.forEach(product -> {
                // Fetch the product name using the product's ID.
                String productName = "";
                try {
                    productName = getProductName(product.getProductId());
                } catch (UnknownHostException ex) {
                    Logger.getLogger(BusinnesTransaction.class.getName()).log(Level.SEVERE, null, ex);
                }

                // Set the fetched product name into the product object.
                product.setProductName(productName);
            });

            // Uncomment the following lines to include enriched transaction details for the customer.
            // Retrieve all transactions associated with the customer's IBAN.
            // List<?> transactions = getTransactions(customer.getIban());
            // Update the customer object with the retrieved transactions.
            // customer.setTransactions(transactions);
        }

        // Return the customer with enriched product details.
        return customer;
    }

    /**
     * Fetches the product name for a given product ID by making an HTTP GET
     * request to the product service.
     *
     * @param id the ID of the product to fetch
     * @return the name of the product
     */
    private String getProductName(long id) throws UnknownHostException {
        // URL of the product service with the given ID
        String urlString = "http://127.0.0.1:8083/product/" + id;
        StringBuilder response = new StringBuilder();
        String name = "";

        try {
            // Establish a connection to the product service
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");  // Set request method to GET
            connection.setConnectTimeout(5000);  // Set connection timeout
            connection.setReadTimeout(5000);     // Set read timeout

            // Get the response code from the connection
            int responseCode = connection.getResponseCode();
            
            // Check if the response code is HTTP_OK (200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Try-with-resources: BufferedReader will be automatically closed at the end of the try block
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;

                    // Append each line of the response to the StringBuilder
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }

                // Parse the response JSON to extract the product name
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.toString());

                // Return the product name if present in the JSON response
                if (jsonResponse.has("name")) {
                    name = jsonResponse.get("name").asText();
                }
            } else {
                // Handle non-OK response codes
                if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                    return "";
                }

                return "Error: " + responseCode;  // Return the HTTP error code if not OK
            }
        } catch (IOException e) {
            // Handle any exceptions related to the connection or reading response
            Logger.getLogger(BusinnesTransaction.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
            throw new UnknownHostException(e.getMessage());
        }

        return name;
    }

    /**
     * Retrieves a list of transactions associated with a given IBAN account.
     * This method uses a {@link WebClient} to make an HTTP GET request to an
     * external transaction service.
     *
     * @param iban the IBAN account for which to retrieve transactions.
     * @return a {@link List} of transactions associated with the given IBAN.
     */
    private List<?> getTransactions(String iban) {
        // Build a WebClient instance configured with a base URL and default headers.
        WebClient build = createWebClient("http://TRANSACTION/transaction");

        try {
            // Execute the HTTP GET request and process the response
            return build.method(HttpMethod.GET)
                    .uri(uriBuilder -> uriBuilder
                    .path("/customer/transactions")
                    .queryParam("ibanAccount", iban)
                    .build())
                    .retrieve() // Send the request
                    .bodyToFlux(Object.class) // Convert the response body to a reactive stream of objects
                    .collectList() // Collect the stream into a single list
                    .timeout(Duration.ofSeconds(5)) // Set a timeout for the request
                    .block(); // Block to wait for the result synchronously
        } catch (WebClientResponseException e) {
            // Handle HTTP client errors (e.g., 4xx and 5xx)
            e.printStackTrace();
        } catch (Exception e) {
            // Handle unexpected errors
            System.err.println("An error occurred while fetching transactions for IBAN: " + iban);
            e.printStackTrace();
        }

        // Return an empty list if an error occurred
        return Collections.emptyList();
    }

    /**
     * Utility method to create and configure a WebClient instance.
     */
    private WebClient createWebClient(String url) {
        // Validate that the URL is not null or empty
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("The URL cannot be null or empty.");
        }

        // Build and return a WebClient instance with custom configuration
        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(client)) // Use a custom Reactor HTTP client
                .baseUrl(url) // Set the base URL for the WebClient
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE) // Set default headers
                .build(); // Build the WebClient instance
    }
}
