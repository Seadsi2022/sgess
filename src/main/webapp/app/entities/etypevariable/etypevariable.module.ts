import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EtypevariableComponent } from './list/etypevariable.component';
import { EtypevariableDetailComponent } from './detail/etypevariable-detail.component';
import { EtypevariableUpdateComponent } from './update/etypevariable-update.component';
import { EtypevariableDeleteDialogComponent } from './delete/etypevariable-delete-dialog.component';
import { EtypevariableRoutingModule } from './route/etypevariable-routing.module';

@NgModule({
  imports: [SharedModule, EtypevariableRoutingModule],
  declarations: [EtypevariableComponent, EtypevariableDetailComponent, EtypevariableUpdateComponent, EtypevariableDeleteDialogComponent],
})
export class EtypevariableModule {}
