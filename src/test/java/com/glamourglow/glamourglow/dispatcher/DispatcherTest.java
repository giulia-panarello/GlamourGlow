
package com.glamourglow.glamourglow.dispatcher;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import net.jqwik.api.Arbitrary;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.ForAll;
import net.jqwik.api.Provide;
import net.jqwik.api.Property;

import org.junit.jupiter.api.Test;

import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DispatcherTest {

    @Provide
    Arbitrary<String> controllerInesistente() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> nome + "Controller.metodo");
    }

    @Provide
    Arbitrary<String> metodoInesistente() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20)
                .map(nome -> "TestController." + nome);
    }

    @Provide
    Arbitrary<String> messaggioErrore() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(30);
    }

    @Test
    void processRequest_controllerActionNull_usaControllerPredefinito()
            throws Exception {

        Dispatcher dispatcher = new Dispatcher();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter())
                .thenReturn(writer);

        when(request.getParameter("controllerAction"))
                .thenReturn(null);

        when(request.getAttribute("viewUrl"))
                .thenReturn("HomeManagement/home");

        when(request.getRequestDispatcher(
                "jsp/HomeManagement/home.jsp"))
                .thenReturn(requestDispatcher);

        assertDoesNotThrow(() ->
                dispatcher.processRequest(request, response)
        );

        verify(response)
                .setContentType("text/html;charset=UTF-8");

        verify(request)
                .getParameter("controllerAction");

        verify(request)
                .getRequestDispatcher(
                        "jsp/HomeManagement/home.jsp"
                );

        verify(requestDispatcher)
                .forward(request, response);

        verify(writer)
                .close();
    }

    @Property
    void processRequest_controllerInesistente_generaServerException(
            @ForAll("controllerInesistente") String controllerAction)
            throws Exception {

        Dispatcher dispatcher = new Dispatcher();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter())
                .thenReturn(writer);

        when(request.getParameter("controllerAction"))
                .thenReturn(controllerAction);

        Exception exception = assertThrows(
                Exception.class,
                () -> dispatcher.processRequest(request, response)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Dispacther Servlet Error")
        );

        verify(response)
                .setContentType("text/html;charset=UTF-8");

        verify(writer)
                .close();
    }

    @Property
    void processRequest_metodoInesistente_generaServerException(
            @ForAll("metodoInesistente") String controllerAction)
            throws Exception {

        Dispatcher dispatcher = new Dispatcher();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter())
                .thenReturn(writer);

        when(request.getParameter("controllerAction"))
                .thenReturn(controllerAction);

        Exception exception = assertThrows(
                Exception.class,
                () -> dispatcher.processRequest(request, response)
        );

        assertTrue(
                exception.getMessage()
                        .contains("Dispacther Servlet Error")
        );

        verify(response)
                .setContentType("text/html;charset=UTF-8");

        verify(writer)
                .close();
    }

    @Property
    void processRequest_getWriterLanciaEccezione(
            @ForAll("messaggioErrore") String messaggioErrore)
            throws Exception {

        Dispatcher dispatcher = new Dispatcher();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter())
                .thenThrow(new RuntimeException(messaggioErrore));

        assertThrows(
                RuntimeException.class,
                () -> dispatcher.processRequest(request, response)
        );

        verify(response)
                .setContentType("text/html;charset=UTF-8");

        verify(response)
                .getWriter();
    }

    @Test
    void doGet_esegueProcessRequest() throws Exception {

        Dispatcher dispatcher = spy(new Dispatcher());

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doNothing().when(dispatcher)
                .processRequest(request, response);

        dispatcher.doGet(request, response);

        verify(dispatcher)
                .processRequest(request, response);
    }

    @Test
    void doPost_esegueProcessRequest() throws Exception {

        Dispatcher dispatcher = spy(new Dispatcher());

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        doNothing().when(dispatcher)
                .processRequest(request, response);

        dispatcher.doPost(request, response);

        verify(dispatcher)
                .processRequest(request, response);
    }

    @Test
    void getServletInfo_restituisceDescrizione() {

        Dispatcher dispatcher = new Dispatcher();

        String result = dispatcher.getServletInfo();

        assertEquals(
                "Short description",
                result
        );
    }

    @Test
    void processRequest_controllerActionValida_esegueControllerEForward()
            throws Exception {

        Dispatcher dispatcher = new Dispatcher();

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        PrintWriter writer = mock(PrintWriter.class);

        when(response.getWriter())
                .thenReturn(writer);

        when(request.getParameter("controllerAction"))
                .thenReturn("HomeManagement.viewhome");

        when(request.getAttribute("viewUrl"))
                .thenReturn("HomeManagement/home");

        when(request.getRequestDispatcher(
                "jsp/HomeManagement/home.jsp"))
                .thenReturn(requestDispatcher);

        dispatcher.processRequest(request, response);

        verify(response)
                .setContentType("text/html;charset=UTF-8");

        verify(request)
                .getParameter("controllerAction");

        verify(request)
                .getRequestDispatcher(
                        "jsp/HomeManagement/home.jsp"
                );

        verify(requestDispatcher)
                .forward(request, response);

        verify(writer)
                .close();
    }
}

