package com.embleon.marketofficial.service.impl;

import com.embleon.marketofficial.App;
import com.embleon.marketofficial.ProductCatalogServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ProductCatalogService {

    private static final Logger logger = LogManager.getLogger(ProductCatalogService.class);

    private static final ProductCatalogService service = new ProductCatalogService();
    private Server server;

    private static ProductCatalogService getInstance() {
        return service;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // Start the RPC server. You shouldn't see any output from gRPC before this.
        logger.info("ProductCatalogService starting.");
        final ProductCatalogService service = ProductCatalogService.getInstance();
        service.start();
        service.blockUntilShutdown();
    }

    private void start() throws IOException {
        int port = Integer.parseInt("9090"/*System.getenv("PORT")*/);

        server =
                ServerBuilder.forPort(port)
                        .addService(new ProductCatalogServiceImpl())
                        .build()
                        .start();
        logger.info("ProductCatalogService started, listening on " + port);
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread() {
                            @Override
                            public void run() {
                                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                                System.err.println("*** shutting down gRPC ads server since JVM is shutting down");
                                ProductCatalogService.this.stop();
                                System.err.println("*** server shut down");
                            }
                        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    private static class ProductCatalogServiceImpl extends ProductCatalogServiceGrpc.ProductCatalogServiceImplBase {
        @Override
        public void listProducts(App.Empty request, StreamObserver<App.ListProductsResponse> responseObserver) {
            App.ListProductsResponse listProductsResponse = App.ListProductsResponse.newBuilder()
                    .addProducts(App.Product.newBuilder().setId("1")
                                                         .setName("Apple iPhone")).build();

            responseObserver.onNext(listProductsResponse);
            responseObserver.onCompleted();
        }

        @Override
        public void getProduct(App.GetProductRequest request, StreamObserver<App.Product> responseObserver) {
            super.getProduct(request, responseObserver);
        }

        @Override
        public void searchProducts(App.SearchProductsRequest request, StreamObserver<App.SearchProductsResponse> responseObserver) {
            super.searchProducts(request, responseObserver);
        }
    }
}
