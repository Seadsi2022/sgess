import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EgroupeComponent } from './list/egroupe.component';
import { EgroupeDetailComponent } from './detail/egroupe-detail.component';
import { EgroupeUpdateComponent } from './update/egroupe-update.component';
import { EgroupeDeleteDialogComponent } from './delete/egroupe-delete-dialog.component';
import { EgroupeRoutingModule } from './route/egroupe-routing.module';

@NgModule({
  imports: [SharedModule, EgroupeRoutingModule],
  declarations: [EgroupeComponent, EgroupeDetailComponent, EgroupeUpdateComponent, EgroupeDeleteDialogComponent],
})
export class EgroupeModule {}
