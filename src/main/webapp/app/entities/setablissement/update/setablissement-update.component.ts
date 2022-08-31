import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SetablissementFormService, SetablissementFormGroup } from './setablissement-form.service';
import { ISetablissement } from '../setablissement.model';
import { SetablissementService } from '../service/setablissement.service';
import { ISstructure } from 'app/entities/sstructure/sstructure.model';
import { SstructureService } from 'app/entities/sstructure/service/sstructure.service';

@Component({
  selector: 'jhi-setablissement-update',
  templateUrl: './setablissement-update.component.html',
})
export class SetablissementUpdateComponent implements OnInit {
  isSaving = false;
  setablissement: ISetablissement | null = null;

  sstructuresSharedCollection: ISstructure[] = [];

  editForm: SetablissementFormGroup = this.setablissementFormService.createSetablissementFormGroup();

  constructor(
    protected setablissementService: SetablissementService,
    protected setablissementFormService: SetablissementFormService,
    protected sstructureService: SstructureService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSstructure = (o1: ISstructure | null, o2: ISstructure | null): boolean => this.sstructureService.compareSstructure(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ setablissement }) => {
      this.setablissement = setablissement;
      if (setablissement) {
        this.updateForm(setablissement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const setablissement = this.setablissementFormService.getSetablissement(this.editForm);
    if (setablissement.id !== null) {
      this.subscribeToSaveResponse(this.setablissementService.update(setablissement));
    } else {
      this.subscribeToSaveResponse(this.setablissementService.create(setablissement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISetablissement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(setablissement: ISetablissement): void {
    this.setablissement = setablissement;
    this.setablissementFormService.resetForm(this.editForm, setablissement);

    this.sstructuresSharedCollection = this.sstructureService.addSstructureToCollectionIfMissing<ISstructure>(
      this.sstructuresSharedCollection,
      setablissement.sstructure
    );
  }

  protected loadRelationshipsOptions(): void {
    this.sstructureService
      .query()
      .pipe(map((res: HttpResponse<ISstructure[]>) => res.body ?? []))
      .pipe(
        map((sstructures: ISstructure[]) =>
          this.sstructureService.addSstructureToCollectionIfMissing<ISstructure>(sstructures, this.setablissement?.sstructure)
        )
      )
      .subscribe((sstructures: ISstructure[]) => (this.sstructuresSharedCollection = sstructures));
  }
}
