import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SstructureComponent } from './list/sstructure.component';
import { SstructureDetailComponent } from './detail/sstructure-detail.component';
import { SstructureUpdateComponent } from './update/sstructure-update.component';
import { SstructureDeleteDialogComponent } from './delete/sstructure-delete-dialog.component';
import { SstructureRoutingModule } from './route/sstructure-routing.module';

@NgModule({
  imports: [SharedModule, SstructureRoutingModule],
  declarations: [SstructureComponent, SstructureDetailComponent, SstructureUpdateComponent, SstructureDeleteDialogComponent],
})
export class SstructureModule {}
