package ru.moysklad.remap_1_2;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import ru.moysklad.remap_1_2.clients.endpoints.ApiChainElement;
import ru.moysklad.remap_1_2.clients.endpoints.ApiEndpoint;
import ru.moysklad.remap_1_2.clients.endpoints.ExportEndpoint;
import ru.moysklad.remap_1_2.entities.Meta;
import ru.moysklad.remap_1_2.entities.MetaEntity;
import ru.moysklad.remap_1_2.entities.Template;
import ru.moysklad.remap_1_2.entities.documents.DocumentEntity;
import ru.moysklad.remap_1_2.responses.ListEntity;
import ru.moysklad.remap_1_2.utils.MockHttpClient;
import ru.moysklad.remap_1_2.utils.TestRandomizers;
import ru.moysklad.remap_1_2.utils.params.ApiParam;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class EndpointsTest implements TestRandomizers {
    private ApiClient api;
    private MockHttpClient mockHttpClient;
    private Set<String> expected, newMethods;

    @Before
    public void init() {
        mockHttpClient = new MockHttpClient();
        api = new ApiClient("test.moysklad", true, "[API_LOGIN]", "[API_PASSWORD]", mockHttpClient);

        ClassLoader classLoader = EndpointsTest.class.getClassLoader();
        try {
            expected = new HashSet<>();
            Collections.addAll(
                    expected,
                    IOUtils.toString(classLoader.getResourceAsStream("methods.csv"), Charset.forName("UTF-8")).split("\r?\n")
            );
        } catch (Exception e) {
            e.printStackTrace();
            fail("Невозможно прочитать файл с эталонами methods.csv");
        }

        newMethods = new HashSet<>();
    }

    @Test
    public void endpointsTest() throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Object entityEntryPoint = api.entity();
        for (Method method : entityEntryPoint.getClass().getMethods()) {
            scanMethod(entityEntryPoint, method);
        }

        if (expected.isEmpty() && newMethods.isEmpty()) return;

        if (!expected.isEmpty() && !newMethods.isEmpty()) fail(
                "Не все методы из файла эталонов были найдены и некоторые текущие методы не были в него добавлены:" +
                        "\n" + expected.stream().sorted().map(s -> " - " + s).collect(Collectors.joining("\n")) +
                        "\n" + newMethods.stream().sorted().map(s -> " + " + s).collect(Collectors.joining("\n"))
        );

        if (!expected.isEmpty()) fail("Не все методы из файла эталонов были найдены:\n " + expected.stream().map(s -> " - " + s).collect(Collectors.joining("\n")));
        fail("Некоторые текущие методы SDK не были найдены в файле эталонов:\n" + newMethods.stream().map(s -> " - " + s).collect(Collectors.joining("\n")));
    }

    private void scanMethod(Object entityEntryPoint, Method method) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!method.isAnnotationPresent(ApiChainElement.class)) return;

        Object client = null;
        if (method.getParameterCount() == 0) {
            client = method.invoke(entityEntryPoint);
        } else if (method.getParameterCount() == 1) {
            if (method.getParameterTypes()[0] == String.class) {
                client = method.invoke(entityEntryPoint, "ID");
            } else {
                fail("Работа с методами, принимающими на вход один параметр типа " + method.getParameterTypes()[0] + ", в тесте ещё не реализована!");
            }
        } else {
            fail("Работа с методами, принимающими на вход " + method.getParameterCount() + " эл., в тесте ещё не реализована!");
        }

        for (Method method1 : client.getClass().getMethods()) {
            if (method1.isAnnotationPresent(ApiEndpoint.class)) {
                mockHttpClient.reset();

                Object resp;
                ArrayList<Object> params = new ArrayList<>();
                for (int i = 0; i < method1.getParameterCount(); i++) {
                    if (method1.getParameterTypes()[i] == File.class) {
                        params.add(new File("test.xls"));
                    } else if (method1.getParameterTypes()[i] == Collection.class) {
                        params.add(new ArrayList());
                    } else if (method1.getParameterTypes()[i] == List.class) {
                        params.add(new ArrayList());
                    } else if (method1.getParameterTypes()[i] == String.class) {
                        params.add("ID");
                    } else if (method1.getParameterTypes()[i] == ApiParam[].class) {
                        params.add(new ApiParam[0]);
                    } else if (method1.getParameterTypes()[i] == Template.class) {
                        params.add(new Template());
                    } else if (method1.getParameterTypes()[i] == ExportEndpoint.PrintRequest[].class) {
                        params.add(new ExportEndpoint.PrintRequest[0]);
                    } else if (method1.getParameterTypes()[i] == boolean.class) {
                        params.add(false);
                    } else if (method1.getParameterTypes()[i] == ListEntity.class) {
                        params.add(new ListEntity());
                    } else if (method1.getParameterTypes()[i] == DocumentEntity.class) {
                        DocumentEntity documentEntity = new DocumentEntity() {
                            @Override
                            public String getId() {
                                return super.getId();
                            }

                            @Override
                            public void setId(String id) {
                                super.setId(id);
                            }
                        };
                        documentEntity.setId("DOCUMENT_ID");
                        documentEntity.setMeta(new Meta());
                        params.add(documentEntity);
                    } else if (DocumentEntity.class.isAssignableFrom(method1.getParameterTypes()[i])) {
                        DocumentEntity documentEntity = (DocumentEntity) method1.getParameterTypes()[i].newInstance();
                        documentEntity.setId("DOCUMENT_ID");
                        params.add(documentEntity);
                    } else if (MetaEntity.class.isAssignableFrom(method1.getParameterTypes()[i])) {
                        MetaEntity entity = (MetaEntity) method1.getParameterTypes()[i].newInstance();
                        entity.setId("ENTITY_ID");
                        params.add(entity);
                    } else if (method1.getParameterTypes()[i].isEnum()) {
                        params.add(randomEnum((Class<? extends Enum>) method1.getParameterTypes()[i]));
                    } else {
                        params.add(method1.getParameterTypes()[i].newInstance());
                    }
                }

                resp = method1.invoke(client, params.toArray(new Object[params.size()]));

                if (mockHttpClient.getLastExecutedRequest() == null) continue;

                assertNotNull(mockHttpClient.getLastExecutedRequest().getFirstHeader("Authorization"));
                assertEquals(
                        "Basic W0FQSV9MT0dJTl06W0FQSV9QQVNTV09SRF0=",
                        mockHttpClient.getLastExecutedRequest().getFirstHeader("Authorization").getValue()
                );

                assertMethod(method1, resp);
            } else if (method1.isAnnotationPresent(ApiChainElement.class)) {
                scanMethod(client, method1);
            }
        }
    }

    private void assertMethod(Method method, Object resp) {
        String methodSignature = mockHttpClient.getLastExecutedRequest().getRequestLine().getMethod() + ";" +
                mockHttpClient.getLastExecutedRequest().getRequestLine().getUri() + ";" +
                Arrays.stream(method.getParameters()).map(p -> p.getType().getSimpleName()).collect(Collectors.joining(",")) + ";" +
                (method.getReturnType().equals(Void.TYPE) ? "void" : resp.getClass().getSimpleName());

        System.out.println(methodSignature);

        if (!expected.contains(methodSignature)) {
            // Если метод не найден в ожидаемых, значит это новый метод, которого нет в эталонах
            newMethods.add(methodSignature);
        } else {
            expected.remove(methodSignature);
        }
    }
}