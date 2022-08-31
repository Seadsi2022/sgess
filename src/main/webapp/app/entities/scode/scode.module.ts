import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ScodeComponent } from './list/scode.component';
import { ScodeDetailComponent } from './detail/scode-detail.component';
import { ScodeUpdateComponent } from './update/scode-update.component';
import { ScodeDeleteDialogComponent } from './delete/scode-delete-dialog.component';
import { ScodeRoutingModule } from './route/scode-routing.module';

@NgModule({
  imports: [SharedModule, ScodeRoutingModule],
  declarations: [ScodeComponent, ScodeDetailComponent, ScodeUpdateComponent, ScodeDeleteDialogComponent],
})
export class ScodeModule {}
