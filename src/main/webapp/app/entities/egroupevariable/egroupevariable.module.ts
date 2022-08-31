import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EgroupevariableComponent } from './list/egroupevariable.component';
import { EgroupevariableDetailComponent } from './detail/egroupevariable-detail.component';
import { EgroupevariableUpdateComponent } from './update/egroupevariable-update.component';
import { EgroupevariableDeleteDialogComponent } from './delete/egroupevariable-delete-dialog.component';
import { EgroupevariableRoutingModule } from './route/egroupevariable-routing.module';

@NgModule({
  imports: [SharedModule, EgroupevariableRoutingModule],
  declarations: [
    EgroupevariableComponent,
    EgroupevariableDetailComponent,
    EgroupevariableUpdateComponent,
    EgroupevariableDeleteDialogComponent,
  ],
})
export class EgroupevariableModule {}
