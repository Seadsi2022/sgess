import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SetablissementComponent } from './list/setablissement.component';
import { SetablissementDetailComponent } from './detail/setablissement-detail.component';
import { SetablissementUpdateComponent } from './update/setablissement-update.component';
import { SetablissementDeleteDialogComponent } from './delete/setablissement-delete-dialog.component';
import { SetablissementRoutingModule } from './route/setablissement-routing.module';

@NgModule({
  imports: [SharedModule, SetablissementRoutingModule],
  declarations: [
    SetablissementComponent,
    SetablissementDetailComponent,
    SetablissementUpdateComponent,
    SetablissementDeleteDialogComponent,
  ],
})
export class SetablissementModule {}
