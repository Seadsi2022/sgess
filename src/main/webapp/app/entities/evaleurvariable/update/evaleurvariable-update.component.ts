import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EvaleurvariableFormService, EvaleurvariableFormGroup } from './evaleurvariable-form.service';
import { IEvaleurvariable } from '../evaleurvariable.model';
import { EvaleurvariableService } from '../service/evaleurvariable.service';
import { IEgroupevariable } from 'app/entities/egroupevariable/egroupevariable.model';
import { EgroupevariableService } from 'app/entities/egroupevariable/service/egroupevariable.service';
import { ISstructure } from 'app/entities/sstructure/sstructure.model';
import { SstructureService } from 'app/entities/sstructure/service/sstructure.service';

@Component({
  selector: 'jhi-evaleurvariable-update',
  templateUrl: './evaleurvariable-update.component.html',
})
export class EvaleurvariableUpdateComponent implements OnInit {
  isSaving = false;
  evaleurvariable: IEvaleurvariable | null = null;

  egroupevariablesSharedCollection: IEgroupevariable[] = [];
  sstructuresSharedCollection: ISstructure[] = [];

  editForm: EvaleurvariableFormGroup = this.evaleurvariableFormService.createEvaleurvariableFormGroup();

  constructor(
    protected evaleurvariableService: EvaleurvariableService,
    protected evaleurvariableFormService: EvaleurvariableFormService,
    protected egroupevariableService: EgroupevariableService,
    protected sstructureService: SstructureService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEgroupevariable = (o1: IEgroupevariable | null, o2: IEgroupevariable | null): boolean =>
    this.egroupevariableService.compareEgroupevariable(o1, o2);

  compareSstructure = (o1: ISstructure | null, o2: ISstructure | null): boolean => this.sstructureService.compareSstructure(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaleurvariable }) => {
      this.evaleurvariable = evaleurvariable;
      if (evaleurvariable) {
        this.updateForm(evaleurvariable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evaleurvariable = this.evaleurvariableFormService.getEvaleurvariable(this.editForm);
    if (evaleurvariable.id !== null) {
      this.subscribeToSaveResponse(this.evaleurvariableService.update(evaleurvariable));
    } else {
      this.subscribeToSaveResponse(this.evaleurvariableService.create(evaleurvariable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaleurvariable>>): void {
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

  protected updateForm(evaleurvariable: IEvaleurvariable): void {
    this.evaleurvariable = evaleurvariable;
    this.evaleurvariableFormService.resetForm(this.editForm, evaleurvariable);

    this.egroupevariablesSharedCollection = this.egroupevariableService.addEgroupevariableToCollectionIfMissing<IEgroupevariable>(
      this.egroupevariablesSharedCollection,
      evaleurvariable.egroupevariable
    );
    this.sstructuresSharedCollection = this.sstructureService.addSstructureToCollectionIfMissing<ISstructure>(
      this.sstructuresSharedCollection,
      evaleurvariable.sstructure
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
            this.evaleurvariable?.egroupevariable
          )
        )
      )
      .subscribe((egroupevariables: IEgroupevariable[]) => (this.egroupevariablesSharedCollection = egroupevariables));

    this.sstructureService
      .query()
      .pipe(map((res: HttpResponse<ISstructure[]>) => res.body ?? []))
      .pipe(
        map((sstructures: ISstructure[]) =>
          this.sstructureService.addSstructureToCollectionIfMissing<ISstructure>(sstructures, this.evaleurvariable?.sstructure)
        )
      )
      .subscribe((sstructures: ISstructure[]) => (this.sstructuresSharedCollection = sstructures));
  }
}
