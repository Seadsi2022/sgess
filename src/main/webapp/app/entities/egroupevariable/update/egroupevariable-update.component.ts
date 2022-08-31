import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EgroupevariableFormService, EgroupevariableFormGroup } from './egroupevariable-form.service';
import { IEgroupevariable } from '../egroupevariable.model';
import { EgroupevariableService } from '../service/egroupevariable.service';
import { IEvariable } from 'app/entities/evariable/evariable.model';
import { EvariableService } from 'app/entities/evariable/service/evariable.service';
import { IEgroupe } from 'app/entities/egroupe/egroupe.model';
import { EgroupeService } from 'app/entities/egroupe/service/egroupe.service';

@Component({
  selector: 'jhi-egroupevariable-update',
  templateUrl: './egroupevariable-update.component.html',
})
export class EgroupevariableUpdateComponent implements OnInit {
  isSaving = false;
  egroupevariable: IEgroupevariable | null = null;

  egroupevariablesSharedCollection: IEgroupevariable[] = [];
  evariablesSharedCollection: IEvariable[] = [];
  egroupesSharedCollection: IEgroupe[] = [];

  editForm: EgroupevariableFormGroup = this.egroupevariableFormService.createEgroupevariableFormGroup();

  constructor(
    protected egroupevariableService: EgroupevariableService,
    protected egroupevariableFormService: EgroupevariableFormService,
    protected evariableService: EvariableService,
    protected egroupeService: EgroupeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEgroupevariable = (o1: IEgroupevariable | null, o2: IEgroupevariable | null): boolean =>
    this.egroupevariableService.compareEgroupevariable(o1, o2);

  compareEvariable = (o1: IEvariable | null, o2: IEvariable | null): boolean => this.evariableService.compareEvariable(o1, o2);

  compareEgroupe = (o1: IEgroupe | null, o2: IEgroupe | null): boolean => this.egroupeService.compareEgroupe(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egroupevariable }) => {
      this.egroupevariable = egroupevariable;
      if (egroupevariable) {
        this.updateForm(egroupevariable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const egroupevariable = this.egroupevariableFormService.getEgroupevariable(this.editForm);
    if (egroupevariable.id !== null) {
      this.subscribeToSaveResponse(this.egroupevariableService.update(egroupevariable));
    } else {
      this.subscribeToSaveResponse(this.egroupevariableService.create(egroupevariable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEgroupevariable>>): void {
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

  protected updateForm(egroupevariable: IEgroupevariable): void {
    this.egroupevariable = egroupevariable;
    this.egroupevariableFormService.resetForm(this.editForm, egroupevariable);

    this.egroupevariablesSharedCollection = this.egroupevariableService.addEgroupevariableToCollectionIfMissing<IEgroupevariable>(
      this.egroupevariablesSharedCollection,
      egroupevariable.suivant
    );
    this.evariablesSharedCollection = this.evariableService.addEvariableToCollectionIfMissing<IEvariable>(
      this.evariablesSharedCollection,
      egroupevariable.evariable
    );
    this.egroupesSharedCollection = this.egroupeService.addEgroupeToCollectionIfMissing<IEgroupe>(
      this.egroupesSharedCollection,
      egroupevariable.egroupe
    );
  }

  protected loadRelationshipsOptions(): void {
    this.egroupevariableService
      .query()
      .pipe(map((res: HttpResponse<IEgroupevariable[]>) => res.body ?? []))
      .pipe(
        map((egroupevariables: IEgroupevariable[]) =>
          this.egroupevariableService.addEgroupevariableToCollectionIfMissing<IEgroupevariable>(
            egroupevariables,
            this.egroupevariable?.suivant
          )
        )
      )
      .subscribe((egroupevariables: IEgroupevariable[]) => (this.egroupevariablesSharedCollection = egroupevariables));

    this.evariableService
      .query()
      .pipe(map((res: HttpResponse<IEvariable[]>) => res.body ?? []))
      .pipe(
        map((evariables: IEvariable[]) =>
          this.evariableService.addEvariableToCollectionIfMissing<IEvariable>(evariables, this.egroupevariable?.evariable)
        )
      )
      .subscribe((evariables: IEvariable[]) => (this.evariablesSharedCollection = evariables));

    this.egroupeService
      .query()
      .pipe(map((res: HttpResponse<IEgroupe[]>) => res.body ?? []))
      .pipe(
        map((egroupes: IEgroupe[]) =>
          this.egroupeService.addEgroupeToCollectionIfMissing<IEgroupe>(egroupes, this.egroupevariable?.egroupe)
        )
      )
      .subscribe((egroupes: IEgroupe[]) => (this.egroupesSharedCollection = egroupes));
  }
}
