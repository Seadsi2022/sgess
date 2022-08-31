import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEgroupevariable } from '../egroupevariable.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../egroupevariable.test-samples';

import { EgroupevariableService } from './egroupevariable.service';

const requireRestSample: IEgroupevariable = {
  ...sampleWithRequiredData,
};

describe('Egroupevariable Service', () => {
  let service: EgroupevariableService;
  let httpMock: HttpTestingController;
  let expectedResult: IEgroupevariable | IEgroupevariable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EgroupevariableService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Egroupevariable', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const egroupevariable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(egroupevariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Egroupevariable', () => {
      const egroupevariable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(egroupevariable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Egroupevariable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Egroupevariable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Egroupevariable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEgroupevariableToCollectionIfMissing', () => {
      it('should add a Egroupevariable to an empty array', () => {
        const egroupevariable: IEgroupevariable = sampleWithRequiredData;
        expectedResult = service.addEgroupevariableToCollectionIfMissing([], egroupevariable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egroupevariable);
      });

      it('should not add a Egroupevariable to an array that contains it', () => {
        const egroupevariable: IEgroupevariable = sampleWithRequiredData;
        const egroupevariableCollection: IEgroupevariable[] = [
          {
            ...egroupevariable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEgroupevariableToCollectionIfMissing(egroupevariableCollection, egroupevariable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Egroupevariable to an array that doesn't contain it", () => {
        const egroupevariable: IEgroupevariable = sampleWithRequiredData;
        const egroupevariableCollection: IEgroupevariable[] = [sampleWithPartialData];
        expectedResult = service.addEgroupevariableToCollectionIfMissing(egroupevariableCollection, egroupevariable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egroupevariable);
      });

      it('should add only unique Egroupevariable to an array', () => {
        const egroupevariableArray: IEgroupevariable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const egroupevariableCollection: IEgroupevariable[] = [sampleWithRequiredData];
        expectedResult = service.addEgroupevariableToCollectionIfMissing(egroupevariableCollection, ...egroupevariableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const egroupevariable: IEgroupevariable = sampleWithRequiredData;
        const egroupevariable2: IEgroupevariable = sampleWithPartialData;
        expectedResult = service.addEgroupevariableToCollectionIfMissing([], egroupevariable, egroupevariable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egroupevariable);
        expect(expectedResult).toContain(egroupevariable2);
      });

      it('should accept null and undefined values', () => {
        const egroupevariable: IEgroupevariable = sampleWithRequiredData;
        expectedResult = service.addEgroupevariableToCollectionIfMissing([], null, egroupevariable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egroupevariable);
      });

      it('should return initial array if no Egroupevariable is added', () => {
        const egroupevariableCollection: IEgroupevariable[] = [sampleWithRequiredData];
        expectedResult = service.addEgroupevariableToCollectionIfMissing(egroupevariableCollection, undefined, null);
        expect(expectedResult).toEqual(egroupevariableCollection);
      });
    });

    describe('compareEgroupevariable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEgroupevariable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEgroupevariable(entity1, entity2);
        const compareResult2 = service.compareEgroupevariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEgroupevariable(entity1, entity2);
        const compareResult2 = service.compareEgroupevariable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEgroupevariable(entity1, entity2);
        const compareResult2 = service.compareEgroupevariable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
