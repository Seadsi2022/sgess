import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EquestionnaireComponent } from './list/equestionnaire.component';
import { EquestionnaireDetailComponent } from './detail/equestionnaire-detail.component';
import { EquestionnaireUpdateComponent } from './update/equestionnaire-update.component';
import { EquestionnaireDeleteDialogComponent } from './delete/equestionnaire-delete-dialog.component';
import { EquestionnaireRoutingModule } from './route/equestionnaire-routing.module';

@NgModule({
  imports: [SharedModule, EquestionnaireRoutingModule],
  declarations: [
    EquestionnaireComponent,
    EquestionnaireDetailComponent,
    EquestionnaireUpdateComponent,
    EquestionnaireDeleteDialogComponent,
  ],
})
export class EquestionnaireModule {}
