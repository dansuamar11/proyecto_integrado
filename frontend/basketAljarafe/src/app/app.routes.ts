import { Routes } from '@angular/router';
import { ActaArbitroComponent } from './components/acta-arbitro/acta-arbitro.component';
import { ArbitroComponent } from './components/arbitro/arbitro.component';
import { CalendarioComponent } from './components/calendario/calendario.component';
import { ClasificacionComponent } from './components/clasificacion/clasificacion.component';
import { ContactoComponent } from './components/contacto/contacto.component';
import { EntrenadorComponent } from './components/entrenador/entrenador.component';
import { EstadisticasComponent } from './components/estadisticas/estadisticas.component';
import { FichaPartidoComponent } from './components/ficha-partido/ficha-partido.component';
import { GerenteComponent } from './components/gerente/gerente.component';
import { InicioComponent } from './components/inicio/inicio.component';
import { LoginComponent } from './components/login/login.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { PanelComponent } from './components/panel/panel.component';
import { SolicitudesContactoComponent } from './components/solicitudes-contacto/solicitudes-contacto.component';
import { rolesGuard } from './core/guards/roles.guard';
import {
  calendarioResolver,
  clasificacionResolver,
  fichaPartidoResolver,
  estadisticasResolver,
  inicioResolver
} from './core/resolvers/public-page.resolver';
import {
  actaArbitroResolver,
  arbitroResolver,
  entrenadorResolver,
  gerenteResolver,
  solicitudesContactoResolver
} from './core/resolvers/private-page.resolver';

export const routes: Routes = [
  {
    path: '',
    component: InicioComponent,
    resolve: {
      datosPagina: inicioResolver
    },
    title: 'Inicio | Basket Aljarafe'
  },
  {
    path: 'inicio',
    component: InicioComponent,
    resolve: {
      datosPagina: inicioResolver
    },
    title: 'Inicio | Basket Aljarafe'
  },
  {
    path: 'clasificacion',
    component: ClasificacionComponent,
    resolve: {
      datosPagina: clasificacionResolver
    },
    title: 'Clasificacion | Basket Aljarafe'
  },
  {
    path: 'calendario',
    component: CalendarioComponent,
    resolve: {
      datosPagina: calendarioResolver
    },
    title: 'Calendario | Basket Aljarafe'
  },
  {
    path: 'partidos/:idPartido',
    component: FichaPartidoComponent,
    resolve: {
      datosPagina: fichaPartidoResolver
    },
    title: 'Ficha partido | Basket Aljarafe'
  },
  {
    path: 'estadisticas',
    component: EstadisticasComponent,
    resolve: {
      datosPagina: estadisticasResolver
    },
    title: 'Estadisticas | Basket Aljarafe'
  },
  {
    path: 'contacto',
    component: ContactoComponent,
    title: 'Contacto | Basket Aljarafe'
  },
  {
    path: 'login',
    component: LoginComponent,
    title: 'Login | Basket Aljarafe'
  },
  {
    path: 'panel',
    component: PanelComponent,
    title: 'Panel | Basket Aljarafe'
  },
  {
    path: 'gerente/administrar',
    component: GerenteComponent,
    canActivate: [rolesGuard(['ROLE_ADMIN', 'ROLE_GERENTE'])],
    resolve: {
      datosPagina: gerenteResolver
    },
    title: 'Gerente | Basket Aljarafe'
  },
  {
    path: 'gerente/solicitudes-contacto',
    component: SolicitudesContactoComponent,
    canActivate: [rolesGuard(['ROLE_ADMIN', 'ROLE_GERENTE'])],
    resolve: {
      datosPagina: solicitudesContactoResolver
    },
    title: 'Solicitudes de contacto | Basket Aljarafe'
  },
  {
    path: 'entrenador/administrar-equipo',
    component: EntrenadorComponent,
    canActivate: [rolesGuard(['ROLE_ENTRENADOR'])],
    resolve: {
      datosPagina: entrenadorResolver
    },
    title: 'Entrenador | Basket Aljarafe'
  },
  {
    path: 'arbitro/ficha-partidos',
    component: ArbitroComponent,
    canActivate: [rolesGuard(['ROLE_ARBITRO'])],
    resolve: {
      datosPagina: arbitroResolver
    },
    title: 'Arbitro | Basket Aljarafe'
  },
  {
    path: 'arbitro/partidos/:idPartido/acta',
    component: ActaArbitroComponent,
    canActivate: [rolesGuard(['ROLE_ARBITRO'])],
    resolve: {
      datosPagina: actaArbitroResolver
    },
    title: 'Acta arbitro | Basket Aljarafe'
  },
  {
    path: '404',
    component: NotFoundComponent,
    title: '404 | Basket Aljarafe'
  },
  {
    path: '**',
    component: NotFoundComponent,
    title: '404 | Basket Aljarafe'
  }
];
