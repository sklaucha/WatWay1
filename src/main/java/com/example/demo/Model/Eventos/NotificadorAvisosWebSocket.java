package com.example.demo.Model.Eventos;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// OBSERVER (GoF): un "suscriptor" del evento AvisoPublicadoEvent.
// Cuando se publica un aviso, lo manda por WebSocket a /topic/avisos
// para quien esté conectado en tiempo real.
// Si mañana se quiere avisar también por mail, se crea OTRO suscriptor
// (por ejemplo NotificadorAvisosEmail) y AvisoService no se toca: OCP.
// AFTER_COMMIT: solo avisa si el aviso realmente se guardó en la base.
@Component
@RequiredArgsConstructor
public class NotificadorAvisosWebSocket {

    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void alPublicarAviso(AvisoPublicadoEvent evento) {
        messagingTemplate.convertAndSend("/topic/avisos", evento.aviso());
    }
}
