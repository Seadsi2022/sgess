import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EeventualiteComponent } from './list/eeventualite.component';
import { EeventualiteDetailComponent } from './detail/eeventualite-detail.component';
import { EeventualiteUpdateComponent } from './update/eeventualite-update.component';
import { EeventualiteDeleteDialogComponent } from './delete/eeventualite-delete-dialog.component';
import { EeventualiteRoutingModule } from './route/eeventualite-routing.module';

@NgModule({
  imports: [SharedModule, EeventualiteRoutingModule],
  declarations: [EeventualiteComponent, EeventualiteDetailComponent, EeventualiteUpdateComponent, EeventualiteDeleteDialogComponent],
})
export class EeventualiteModule {}
