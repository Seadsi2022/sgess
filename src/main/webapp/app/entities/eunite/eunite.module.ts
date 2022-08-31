import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EuniteComponent } from './list/eunite.component';
import { EuniteDetailComponent } from './detail/eunite-detail.component';
import { EuniteUpdateComponent } from './update/eunite-update.component';
import { EuniteDeleteDialogComponent } from './delete/eunite-delete-dialog.component';
import { EuniteRoutingModule } from './route/eunite-routing.module';

@NgModule({
  imports: [SharedModule, EuniteRoutingModule],
  declarations: [EuniteComponent, EuniteDetailComponent, EuniteUpdateComponent, EuniteDeleteDialogComponent],
})
export class EuniteModule {}
